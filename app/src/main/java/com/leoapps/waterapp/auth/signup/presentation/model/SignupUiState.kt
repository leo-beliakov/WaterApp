package com.leoapps.waterapp.auth.signup.presentation.model

data class SignupUiState(
    val name: String = "",
    val loading: Boolean = false,
    val email: String = "",
    val password: String = "",
    val showNameInvalidError: Boolean = false,
    val showEmailInvalidError: Boolean = false,
    val passwordStrengths: List<PasswordStrengthItemState> = listOf(),
    val termsAccepted: Boolean = false,
    val createAccountButtonEnabled: Boolean = false
)