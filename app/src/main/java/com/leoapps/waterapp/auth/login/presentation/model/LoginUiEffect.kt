package com.leoapps.waterapp.auth.login.presentation.model

sealed interface LoginUiEffect {
    object NavigateToSignUp : LoginUiEffect
    object NavigateClose : LoginUiEffect
    object ShowAuthFailed : LoginUiEffect
    object RequestFacebookAuth : LoginUiEffect
    object RequestGoogleAuth : LoginUiEffect
}