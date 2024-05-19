package com.leoapps.waterapp.auth.common.data

import androidx.credentials.GetCredentialResponse
import com.facebook.login.LoginResult
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import com.leoapps.waterapp.auth.common.domain.model.AuthException
import com.leoapps.waterapp.auth.common.domain.model.EmailCredential
import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    private val currentUser = MutableStateFlow<User?>(null)

    init {
        auth.addAuthStateListener { authInfo ->
            Timber.i("Firebase Auth State Changed email: ${authInfo.currentUser?.email}")
            currentUser.value = authInfo.currentUser?.let { firebaseUser ->
                User(
                    id = firebaseUser.uid,
                    name = firebaseUser.displayName,
                    email = firebaseUser.email ?: ""
                )
            }
        }
    }

    override fun getCurrentUserAsFlow(): Flow<User?> {
        return currentUser.asStateFlow()
    }

    override suspend fun deleteUser() = flow<TaskResult<Unit>> {
        emit(TaskResult.Loading)

        auth.currentUser
            ?.delete()
            ?.await()
            ?: throw IllegalStateException("User is not logged in")

        emit(TaskResult.Success(Unit))
    }.catch {
        Timber.e(it, "Delete User Failed")
        emit(TaskResult.Failure(it))
    }.flowOn(Dispatchers.IO)

    override suspend fun logOut() = withContext(Dispatchers.IO) {
        auth.signOut()
    }

    //FirebaseAuthUserCollisionException - The email address is already in use by another account.
    //FirebaseNetworkException - No connection
    override fun createAccount(
        name: String,
        email: String,
        password: String
    ) = flow<TaskResult<User>> {
        emit(TaskResult.Loading)

        val authResult = auth
            .createUserWithEmailAndPassword(email, password)
            .await()

        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }

        authResult.user
            ?.updateProfile(profileUpdates)
            ?.await()

        val user = authResult?.user ?: throw AuthException("User is null")

        emit(TaskResult.Success(user.mapToDomain()))
    }.catch {
        Timber.e(it, "Create Account Failed")
        emit(TaskResult.Failure(it))
    }.flowOn(Dispatchers.IO)

    //FirebaseAuthInvalidCredentialsException
    override fun logIn(
        credentials: Any
    ) = flow<TaskResult<User>> {
        emit(TaskResult.Loading)

        val firebaseUser = when (credentials) {
            is EmailCredential -> logInWithEmail(credentials)
            is GetCredentialResponse -> loginWithGoogle(credentials)
            is LoginResult -> loginWithFacebook(credentials)
            else -> throw AuthException("Unsupported credentials")
        }

        emit(TaskResult.Success(firebaseUser.mapToDomain()))
    }.catch {
        Timber.e(it, "Login Failed")
        emit(TaskResult.Failure(it))
    }.flowOn(Dispatchers.IO)

    private suspend fun loginWithFacebook(credential: LoginResult): FirebaseUser {
        //Create a Facebook Credential Token from the credential data
        val token = credential.accessToken.token
        //Create a Firebase credential object using the Facebook Auth provider
        val firebaseCredential = FacebookAuthProvider.getCredential(token)

        return logInWithFirebaseCredentials(firebaseCredential)
    }

    private suspend fun loginWithGoogle(credential: GetCredentialResponse): FirebaseUser {
        //Create a Google Credential Token from the credential data
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(
            credential.credential.data
        )
        //Create a Firebase credential object using the Google Auth provider
        val firebaseCredential = GoogleAuthProvider.getCredential(
            googleIdTokenCredential.idToken,
            null
        )

        return logInWithFirebaseCredentials(firebaseCredential)
    }

    private suspend fun logInWithEmail(credential: EmailCredential): FirebaseUser {
        val authResult = auth.signInWithEmailAndPassword(
            credential.email,
            credential.password
        ).await()

        return authResult.user ?: throw AuthException("User is null")
    }

    private suspend fun logInWithFirebaseCredentials(credential: AuthCredential): FirebaseUser {
        val authResult = auth.signInWithCredential(credential).await()
        return authResult.user ?: throw AuthException("User is null")
    }
}

private fun FirebaseUser.mapToDomain(): User {
    return User(
        id = uid,
        name = displayName,
        email = email
    )
}