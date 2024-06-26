package com.leoapps.waterapp.common.presentation.composables.tab_bar

import androidx.compose.ui.graphics.Color
import com.leoapps.waterapp.common.presentation.theme.NavyDark
import com.leoapps.waterapp.common.presentation.theme.White

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