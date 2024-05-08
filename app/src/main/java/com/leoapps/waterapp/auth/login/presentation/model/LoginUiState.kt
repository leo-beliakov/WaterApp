package com.leoapps.waterapp.auth.login.presentation.model

import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButtonState

data class LoginUiState(
    val email: String = "",
    val showEmailInvalidError: Boolean = false,
    val password: String = "",
    val buttonState: ProgressButtonState = ProgressButtonState(
        isEnabled = false,
        isLoading = false,
        textResId = R.string.common_login,
    )
)