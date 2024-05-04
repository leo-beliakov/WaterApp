package com.leoapps.waterapp.auth.signup.presentation.model

data class SignupUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val termsAccepted: Boolean = false,
)