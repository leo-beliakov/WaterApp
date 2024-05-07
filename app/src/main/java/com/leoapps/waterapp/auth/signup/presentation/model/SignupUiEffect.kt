package com.leoapps.waterapp.auth.signup.presentation.model

sealed interface SignupUiEffect {
    data class OpenUrl(
        val url: String
    ) : SignupUiEffect

    object GoBack : SignupUiEffect
    object CloseAuth : SignupUiEffect
}