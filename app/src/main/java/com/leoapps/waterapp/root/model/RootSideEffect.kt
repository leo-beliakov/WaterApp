package com.leoapps.waterapp.root.model

sealed interface RootSideEffect {
    data class NavigateToTab(
        val route: String,
    ) : RootSideEffect
}