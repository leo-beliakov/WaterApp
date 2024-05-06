package com.leoapps.waterapp.profile.presentation.model

sealed interface ProfileUiEffect {
    object OpenLogin : ProfileUiEffect
}