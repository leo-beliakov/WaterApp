package com.leoapps.waterapp.home.day.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.leoapps.waterapp.R
import com.leoapps.waterapp.ui.theme.Coffee
import com.leoapps.waterapp.ui.theme.Green
import com.leoapps.waterapp.ui.theme.PurpleLight

enum class BeverageItem(
    @DrawableRes val iconResId: Int,
    @StringRes val titleResId: Int,
    val color: Color,
) {
    WATER(
        iconResId = R.drawable.ic_water,
        titleResId = R.string.home_beverage_juice_title,
        color = PurpleLight
    ),
    COFFEE(
        iconResId = R.drawable.ic_coffee,
        titleResId = R.string.home_beverage_water_title,
        color = Coffee
    ),
    JUICE(
        iconResId = R.drawable.ic_juice,
        titleResId = R.string.home_beverage_coffee_title,
        color = Green
    ),
    COLA(
        iconResId = R.drawable.ic_cola,
        titleResId = R.string.home_beverage_cola_title,
        color = PurpleLight
    ),
}