package com.leoapps.waterapp.auth.signup.presentation.model

sealed interface SignUpUiEffect {
    object NavigateBack : SignUpUiEffect
    object NavigateCloseAuth : SignUpUiEffect
    object ShowAuthFailed : SignUpUiEffect
    object RequestFacebookAuth : SignUpUiEffect
    object RequestGoogleAuth : SignUpUiEffect

    data class OpenUrl(
        val url: String
    ) : SignUpUiEffect
}