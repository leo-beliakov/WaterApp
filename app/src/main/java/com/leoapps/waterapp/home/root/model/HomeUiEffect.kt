package com.leoapps.waterapp.home.root.model

sealed interface HomeUiEffect {
    data class NavigateToTab(
        val tab: HomeTab,
    ) : HomeUiEffect
}