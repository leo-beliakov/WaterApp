package com.leoapps.waterapp.auth.common.data

import com.facebook.login.LoginResult
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.leoapps.waterapp.auth.common.domain.model.AuthException
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FacebookAuthHelper @Inject constructor(
    val auth: FirebaseAuth,
) {
    suspend fun handleSuccessSignIn(
        result: LoginResult,
    ) = flow<TaskResult<FirebaseUser>> {
        try {
            emit(TaskResult.Loading)
            val firebaseCredential = retrieveFirebaseCredential(result)
            val user = loginToFirebase(firebaseCredential)
            emit(TaskResult.Success(user))
        } catch (e: AuthException) {
            emit(TaskResult.Failure(e))
        }
    }

    private fun retrieveFirebaseCredential(result: LoginResult): AuthCredential {
        return try {
            val token = result.accessToken.token
            FacebookAuthProvider.getCredential(token)
        } catch (e: FirebaseAuthException) {
            throw AuthException("Failed to parse credential")
        }
    }

    private suspend fun loginToFirebase(credential: AuthCredential): FirebaseUser {
        return try {
            val authResult = Firebase.auth.signInWithCredential(credential).await()
            authResult.user ?: throw AuthException("User is null")
        } catch (e: FirebaseException) {
            throw AuthException("Failed to authenticate")
        }
    }
}