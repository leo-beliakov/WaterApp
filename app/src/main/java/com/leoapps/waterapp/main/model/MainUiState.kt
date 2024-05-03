package com.leoapps.waterapp.main.model

data class MainUiState(
    val selectedTab: MainTab = MainTab.Home,
    val tabs: List<MainTab> = listOf(
        MainTab.Home,
        MainTab.Bottle,
        MainTab.Profile
    )
)