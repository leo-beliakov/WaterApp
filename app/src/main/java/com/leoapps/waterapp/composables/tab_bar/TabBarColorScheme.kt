package com.leoapps.waterapp.composables.tab_bar

import androidx.compose.ui.graphics.Color
import com.leoapps.waterapp.ui.theme.NavyDark
import com.leoapps.waterapp.ui.theme.White

enum class TabBarColorScheme(
    val primaryColor: Color,
    val accentColor: Color,
) {
    PRIMARY(
        primaryColor = White,
        accentColor = NavyDark,
    ),
    INVERTED(
        primaryColor = NavyDark,
        accentColor = White,
    )
}