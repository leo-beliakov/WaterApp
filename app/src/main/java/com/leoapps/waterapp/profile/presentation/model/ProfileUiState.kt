package com.leoapps.waterapp.profile.presentation.model

import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButtonState

data class ProfileUiState(
    val username: String = "",
    val email: String = "",
    val goal: String = "",
    val progress: String = "",
    val previousRecords: String = "",
    val profileInfoVisible: Boolean = false,
    val loginButtonVisible: Boolean = false,
    val deleteButtonState: ProgressButtonState = ProgressButtonState(
        isEnabled = true,
        isLoading = false,
        textResId = R.string.profile_delete_account,
    )
)