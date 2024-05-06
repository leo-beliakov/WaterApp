package com.leoapps.waterapp.common.presentation.composables.progress_button

import androidx.annotation.StringRes

data class ProgressButtonState(
    val isEnabled: Boolean,
    val isLoading: Boolean,
    @StringRes val textResId: Int
)