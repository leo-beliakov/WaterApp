package com.leoapps.waterapp.home.model

sealed interface HomeUiEffect {
    data class NavigateToTab(
        val tab: HomeTab,
    ) : HomeUiEffect
}