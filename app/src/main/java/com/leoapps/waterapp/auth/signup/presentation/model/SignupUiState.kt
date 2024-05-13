package com.leoapps.waterapp.auth.signup.presentation.model

import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButtonState

data class SignupUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val showNameInvalidError: Boolean = false,
    val showEmailInvalidError: Boolean = false,
    val passwordStrengths: List<PasswordStrengthItemState> = listOf(),
    val termsAccepted: Boolean = false,
    val buttonState: ProgressButtonState = ProgressButtonState(
        isEnabled = false,
        isLoading = false,
        textResId = R.string.signup_button_create,
    )
)