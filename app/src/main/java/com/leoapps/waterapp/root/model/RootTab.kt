package com.leoapps.waterapp.root.model

import com.leoapps.waterapp.R
import com.leoapps.waterapp.composables.tab_bar.TabBarTab

sealed class RootTab(
    override val route: String,
    override val titleResId: Int? = null,
    override val iconResId: Int? = null
) : TabBarTab {
    object Home : RootTab(
        route = "home",
        iconResId = R.drawable.ic_drop,
    )

    object Bottle : RootTab(
        route = "bottle",
        iconResId = R.drawable.ic_bottle,
    )

    object Profile : RootTab(
        route = "profile",
        iconResId = R.drawable.ic_profile,
    )
}