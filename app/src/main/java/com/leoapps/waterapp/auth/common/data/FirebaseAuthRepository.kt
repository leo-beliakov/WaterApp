package com.leoapps.waterapp.auth.common.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor() : AuthRepository {

    private val auth by lazy { Firebase.auth }
    override fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun signupUser(
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        suspendCancellableCoroutine<Boolean> { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        continuation.resumeWith(Result.success(true))

                    } else {
                        val exception = task.exception
                        continuation.resumeWith(Result.success(false))
                    }
                }
        }
    }
}