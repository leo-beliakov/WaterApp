package com.leoapps.waterapp.auth.common.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException

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
    suspend fun auth(
        onSuccess: (GetCredentialResponse) -> Unit,
        onFailure: (GetCredentialException) -> Unit,
        request: GetCredentialRequest,
    ) {
        try {
            val authResult = credentialManager.getCredential(context, request)
            onSuccess(authResult)
        } catch (e: GetCredentialException) {
            onFailure(e)
        }
    }
}