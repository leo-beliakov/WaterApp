package com.leoapps.waterapp.root.model

sealed interface RootUiEffect {
    data class NavigateToTab(
        val tab: RootTab,
    ) : RootUiEffect
}