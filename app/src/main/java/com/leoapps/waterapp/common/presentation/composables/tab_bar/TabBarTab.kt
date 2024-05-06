package com.leoapps.waterapp.common.presentation.composables.tab_bar

interface TabBarTab {
    val route: String
    val iconResId: Int?
    val titleResId: Int?
}