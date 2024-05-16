package com.leoapps.waterapp.auth.common.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import com.leoapps.waterapp.auth.common.domain.model.AuthException
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
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    private val currentUser = MutableStateFlow<User?>(null)

    init {
        auth.addAuthStateListener { authInfo ->
            currentUser.value = authInfo.currentUser?.let { firebaseUser ->
                User(
                    id = firebaseUser.uid,
                    name = firebaseUser.displayName
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
        emit(TaskResult.Failure(it))
    }.flowOn(Dispatchers.IO)

    override suspend fun logoutUser() = withContext(Dispatchers.IO) {
        auth.signOut()
    }

    //FirebaseAuthInvalidCredentialsException
    override suspend fun signinUser(
        email: String,
        password: String
    ) = flow<TaskResult<FirebaseUser>> {
        emit(TaskResult.Loading)

        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        val user = authResult.user ?: throw AuthException("User is null")

        emit(TaskResult.Success(user))
    }.catch {
        emit(TaskResult.Failure(it))
    }.flowOn(Dispatchers.IO)

    //FirebaseAuthUserCollisionException - The email address is already in use by another account.
    //FirebaseNetworkException - No connection
    override fun signupUser(
        name: String,
        email: String,
        password: String
    ) = flow<TaskResult<FirebaseUser>> {
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

        emit(TaskResult.Success(user))
    }.catch {
        emit(TaskResult.Failure(it))
    }.flowOn(Dispatchers.IO)
}