package com.leoapps.waterapp.auth.login.presentation.model

import androidx.credentials.GetCredentialRequest

sealed interface LoginUiEffect {
    object NavigateToSignUp : LoginUiEffect
    object NavigateClose : LoginUiEffect
    object ShowAuthFailed : LoginUiEffect
    object RequestFacebookAuth : LoginUiEffect
    class RequestGoogleAuth(
        val request: GetCredentialRequest
    ) : LoginUiEffect
}