package com.leoapps.waterapp.main.model

sealed interface MainUiEffect {
    data class NavigateToTab(
        val tab: MainTab,
    ) : MainUiEffect
}