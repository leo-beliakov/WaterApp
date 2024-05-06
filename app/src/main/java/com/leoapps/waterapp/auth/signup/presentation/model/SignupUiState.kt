package com.leoapps.waterapp.auth.signup.presentation.model

import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButtonState

data class SignupUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val passwordStrengths: List<PasswordStrengthItemState> = listOf(
        //todo probably shouldn't be here
        PasswordStrengthItemState(
            textResId = R.string.password_validation_digits,
            checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
        ),
        PasswordStrengthItemState(
            textResId = R.string.password_validation_lowercase,
            checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
        ),
        PasswordStrengthItemState(
            textResId = R.string.password_validation_uppercase,
            checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
        ),
        PasswordStrengthItemState(
            textResId = R.string.password_validation_special_caracter,
            checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
        ),
        PasswordStrengthItemState(
            textResId = R.string.password_validation_white_spaces,
            checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
        ),
        PasswordStrengthItemState(
            textResId = R.string.password_validation_length,
            checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
        ),
    ),
    val termsAccepted: Boolean = false,
    val buttonState: ProgressButtonState = ProgressButtonState(
        isEnabled = false,
        isLoading = false,
        textResId = R.string.sugnup_button_create,
    )
)