package com.leoapps.waterapp.home.day.model

sealed interface HomeDayUiEffect {
    data class AnimateProgressTo(val progress: Float) : HomeDayUiEffect
}