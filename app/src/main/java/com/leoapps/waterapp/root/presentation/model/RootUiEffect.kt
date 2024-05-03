package com.leoapps.waterapp.root.presentation.model

sealed interface RootUiEffect {
    object GoToOnboarding : RootUiEffect
    object GoToMain : RootUiEffect
}