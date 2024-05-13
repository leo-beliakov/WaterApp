package com.leoapps.waterapp.auth.common.data

import androidx.credentials.Credential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.leoapps.waterapp.auth.common.domain.model.AuthException
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val SERVER_CLIENT_ID =
    "1006655469675-f3algbiterhbl5qk8a5ck8kirngo5ufu.apps.googleusercontent.com"

class GoogleAuthHelper @Inject constructor(
    val auth: FirebaseAuth,
) {
    val signInRequest: GetCredentialRequest
        get() {
            val googleIdOption = GetSignInWithGoogleOption.Builder(SERVER_CLIENT_ID)
                .build()

            return GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .setPreferImmediatelyAvailableCredentials(false)
                .build()
        }

    suspend fun handleSuccessSignIn(
        result: GetCredentialResponse,
    ) = flow<TaskResult<FirebaseUser>> {
        try {
            emit(TaskResult.Loading)
            val firebaseCredential = retrieveFirebaseCredential(result.credential)
            val user = loginToFirebase(firebaseCredential)
            emit(TaskResult.Success(user))
        } catch (e: AuthException) {
            emit(TaskResult.Failure(e))
        }
    }

    private fun retrieveFirebaseCredential(credential: Credential): AuthCredential {
        return try {
            //Create a GoogleIdTokenCredential object from the credential data
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            //Create a Firebase credential object using the Google ID token
            GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
        } catch (e: GoogleIdTokenParsingException) {
            throw AuthException("Failed to parse credential")
        }
    }

    private suspend fun loginToFirebase(firebaseCredential: AuthCredential): FirebaseUser {
        return try {
            val authResult = auth.signInWithCredential(firebaseCredential).await()
            authResult.user ?: throw AuthException("User is null")
        } catch (e: FirebaseException) {
            throw AuthException("Failed to authenticate")
        }
    }
}