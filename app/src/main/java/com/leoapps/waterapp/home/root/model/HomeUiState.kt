package com.leoapps.waterapp.home.root.model

data class HomeUiState(
    val selectedTab: HomeTab = HomeTab.Day,
    val tabs: List<HomeTab> = listOf(
        HomeTab.Day,
        HomeTab.Week
    )
)