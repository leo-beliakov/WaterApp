package com.leoapps.waterapp.auth.login.presentation.model

sealed interface LoginUiEffect {
    object OpenSignUp : LoginUiEffect
    object GoBack : LoginUiEffect
}