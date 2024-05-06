package com.leoapps.waterapp.profile.presentation.model

data class ProfileUiState(
    val username: String = "",
    val profileInfoVisible: Boolean = false,
    val loginButtonVisible: Boolean = false
)