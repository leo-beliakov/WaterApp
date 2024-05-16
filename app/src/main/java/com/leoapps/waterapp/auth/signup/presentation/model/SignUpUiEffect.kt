package com.leoapps.waterapp.auth.signup.presentation.model

import androidx.credentials.GetCredentialRequest

sealed interface SignUpUiEffect {
    object NavigateBack : SignUpUiEffect
    object NavigateCloseAuth : SignUpUiEffect
    object ShowAuthFailed : SignUpUiEffect
    object RequestFacebookAuth : SignUpUiEffect

    class RequestGoogleAuth(
        val request: GetCredentialRequest
    ) : SignUpUiEffect

    data class OpenUrl(
        val url: String
    ) : SignUpUiEffect
}