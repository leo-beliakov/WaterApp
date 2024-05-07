package com.leoapps.waterapp.auth.common.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import com.leoapps.waterapp.common.domain.task_result.isFailure
import com.leoapps.waterapp.common.domain.task_result.mapToUnit
import com.leoapps.waterapp.common.utils.asSuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor() : AuthRepository {

    private val auth by lazy { Firebase.auth }
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

    override suspend fun deleteUser(): TaskResult<Unit> = withContext(Dispatchers.IO) {
        val deleteUserResult = auth.currentUser
            ?.delete()
            ?.asSuspend()

        return@withContext deleteUserResult?.mapToUnit()
            ?: TaskResult.Failure(IllegalStateException("User is not logged in"))
    }

    override suspend fun logoutUser() = withContext(Dispatchers.IO) {
        auth.signOut()
    }

    //FirebaseAuthInvalidCredentialsException
    override suspend fun signinUser(
        email: String,
        password: String
    ): TaskResult<Unit> = withContext(Dispatchers.IO) {
        val signinResult = auth
            .signInWithEmailAndPassword(email, password)
            .asSuspend()

        return@withContext signinResult.mapToUnit()
    }

    //FirebaseAuthUserCollisionException - The email address is already in use by another account.
    //FirebaseNetworkException - No connection
    override suspend fun signupUser(
        name: String,
        email: String,
        password: String
    ): TaskResult<Unit> = withContext(Dispatchers.IO) {
        val signupResult = auth
            .createUserWithEmailAndPassword(email, password)
            .asSuspend()

        if (signupResult.isFailure()) return@withContext signupResult

        val profileUpdates = userProfileChangeRequest { displayName = name }
        val updateResult = auth.currentUser
            ?.updateProfile(profileUpdates)
            ?.asSuspend()

        return@withContext updateResult?.mapToUnit()
            ?: TaskResult.Failure(IllegalStateException("Name not updated"))
    }
}