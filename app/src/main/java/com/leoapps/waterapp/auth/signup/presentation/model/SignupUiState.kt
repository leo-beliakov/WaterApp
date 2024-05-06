package com.leoapps.waterapp.auth.signup.presentation.model

import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButtonState

data class SignupUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val termsAccepted: Boolean = false,
    val buttonState: ProgressButtonState = ProgressButtonState(
        isEnabled = false,
        isLoading = false,
        textResId = R.string.sugnup_button_create,
    )
)