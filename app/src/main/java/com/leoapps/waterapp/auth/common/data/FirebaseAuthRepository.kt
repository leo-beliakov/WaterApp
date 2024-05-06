package com.leoapps.waterapp.auth.common.data

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import com.leoapps.waterapp.auth.common.domain.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
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

    override fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun logoutUser() = withContext(Dispatchers.IO) {
        auth.signOut()
    }

    //FirebaseAuthUserCollisionException - The email address is already in use by another account.
    //FirebaseNetworkException - No connection
    override suspend fun signupUser(
        name: String,
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        //todo track Result of the operation
        val signupResult = auth.createUserWithEmailAndPassword(email, password).asSuspend()
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        signupResult.user?.updateProfile(profileUpdates)?.asSuspend()
        return@withContext true
    }

    suspend fun <T> Task<T>.asSuspend() = suspendCancellableCoroutine<T> { continuation ->
        this.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                continuation.resumeWith(Result.success(task.result))
            } else {
                val exception = task.exception ?: UnknownError("Task failed")
                continuation.resumeWith(Result.failure(exception))
            }
        }
    }
}