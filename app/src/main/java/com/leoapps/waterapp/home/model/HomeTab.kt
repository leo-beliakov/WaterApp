package com.leoapps.waterapp.home.model

import com.leoapps.waterapp.R
import com.leoapps.waterapp.composables.tab_bar.TabBarTab

sealed class HomeTab(
    override val route: String,
    override val titleResId: Int? = null,
    override val iconResId: Int? = null
) : TabBarTab {
    object Day : HomeTab(
        route = "day",
        titleResId = R.string.home_navbar_tab_day,
    )

    object Week : HomeTab(
        route = "week",
        titleResId = R.string.home_navbar_tab_week,
    )
}