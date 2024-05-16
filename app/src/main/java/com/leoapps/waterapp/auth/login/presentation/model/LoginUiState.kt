package com.leoapps.waterapp.auth.login.presentation.model

data class LoginUiState(
    val loading: Boolean = false,
    val email: String = "",
    val showEmailInvalidError: Boolean = false,
    val password: String = "",
    val loginButtonEnabled: Boolean = false
)