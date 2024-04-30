package com.leoapps.waterapp.root.model

data class RootUiState(
    val selectedTab: RootTab = RootTab.Home,
    val tabs: List<RootTab> = listOf(
        RootTab.Home,
        RootTab.Bottle,
        RootTab.Profile
    )
)