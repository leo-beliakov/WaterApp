package com.leoapps.waterapp.auth.common.data

import android.util.Log
import com.google.android.gms.tasks.Task
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
        name: String,
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        Log.d("MyTag", "signupUser")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.d("MyTag", "auth task successed")
            }
            .addOnCanceledListener {
                Log.d("MyTag", "auth task canceled")
            }
            .addOnFailureListener { ex ->
                Log.d("MyTag", "auth task failure $ex")
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("MyTag", "auth task successful")

                } else {
                    Log.d("MyTag", "auth task failed = ${task.exception}")
                }
            }

        Log.d("MyTag", "signupUser finish")
        return@withContext true
    }

    suspend fun <T> Task<T>.asSuspend() = suspendCancellableCoroutine<T> { continuation ->
        this.addOnCanceledListener {
            Log.d("MyTag", "auth task canceled")
        }
        this.addOnFailureListener { ex ->
            Log.d("MyTag", "auth task failure $ex")
        }
        this.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("MyTag", "auth task successful")
                continuation.resumeWith(Result.success(task.result))

            } else {
                Log.d("MyTag", "auth task failed = ${task.exception}")
                val exception = task.exception ?: UnknownError("Task failed")
                continuation.resumeWith(Result.failure(exception))
            }
        }
    }
}