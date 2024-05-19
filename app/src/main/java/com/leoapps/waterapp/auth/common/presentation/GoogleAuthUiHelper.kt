package com.leoapps.waterapp.auth.common.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption

private const val SERVER_CLIENT_ID =
    "1006655469675-f3algbiterhbl5qk8a5ck8kirngo5ufu.apps.googleusercontent.com"

@Composable
fun rememberGoogleAuthHelper(): GoogleAuthUiHelper {
    val context = LocalContext.current
    val credentialManager = CredentialManager.create(context.applicationContext)

    return remember {
        GoogleAuthUiHelper(
            context = context,
            credentialManager = credentialManager
        )
    }
}

class GoogleAuthUiHelper(
    private val context: Context,
    private val credentialManager: CredentialManager,
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

    suspend fun auth(
        onSuccess: (GetCredentialResponse) -> Unit,
        onFailure: (GetCredentialException) -> Unit,
    ) {
        try {
            val authResult = credentialManager.getCredential(context, signInRequest)
            onSuccess(authResult)
        } catch (e: GetCredentialException) {
            onFailure(e)
        }
    }
}