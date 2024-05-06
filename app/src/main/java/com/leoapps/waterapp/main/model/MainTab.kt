package com.leoapps.waterapp.main.model

import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.presentation.composables.tab_bar.TabBarTab

sealed class MainTab(
    override val route: String,
    override val titleResId: Int? = null,
    override val iconResId: Int? = null
) : TabBarTab {
    object Home : MainTab(
        route = "home",
        iconResId = R.drawable.ic_drop,
    )

    object Bottle : MainTab(
        route = "bottle",
        iconResId = R.drawable.ic_bottle,
    )

    object Profile : MainTab(
        route = "profile",
        iconResId = R.drawable.ic_profile,
    )
}