package com.leoapps.waterapp.auth.signup.presentation.model

sealed interface SignupUiEffect {
    object GoBack : SignupUiEffect
}