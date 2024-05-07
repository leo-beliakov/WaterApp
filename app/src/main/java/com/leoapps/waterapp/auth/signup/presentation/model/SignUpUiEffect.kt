package com.leoapps.waterapp.auth.signup.presentation.model

sealed interface SignUpUiEffect {
    data class OpenUrl(
        val url: String
    ) : SignUpUiEffect

    object GoBack : SignUpUiEffect
    object CloseAuth : SignUpUiEffect
}