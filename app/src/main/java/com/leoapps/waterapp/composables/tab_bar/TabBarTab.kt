package com.leoapps.waterapp.composables.tab_bar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

typealias TabId = String

data class TabBarTab(
    val id: TabId,
    @DrawableRes val iconResId: Int? = null,
    @StringRes val titleResId: Int? = null,
)
