package com.leoapps.waterapp.profile.presentation.model

data class ProfileUiState(
    val username: String? = null,
    val profileInfoVisible: Boolean = false,
    val loginButtonVisible: Boolean = false
)