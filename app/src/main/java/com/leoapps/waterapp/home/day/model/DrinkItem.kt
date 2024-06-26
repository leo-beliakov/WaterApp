package com.leoapps.waterapp.home.day.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.presentation.theme.Coffee
import com.leoapps.waterapp.common.presentation.theme.Green
import com.leoapps.waterapp.common.presentation.theme.PurpleLight
import com.leoapps.waterapp.water.domain.model.Drink

enum class DrinkItem(
    val type: Drink,
    @DrawableRes val iconResId: Int,
    @StringRes val titleResId: Int,
    val waterBalanceDelta: Int,
    val color: Color,
) {
    WATER(
        type = Drink.WATER,
        iconResId = R.drawable.ic_water,
        titleResId = R.string.home_beverage_juice_title,
        waterBalanceDelta = 250,
        color = PurpleLight
    ),
    COFFEE(
        type = Drink.COFFEE,
        iconResId = R.drawable.ic_coffee,
        titleResId = R.string.home_beverage_water_title,
        waterBalanceDelta = -100,
        color = Coffee
    ),
    JUICE(
        type = Drink.JUICE,
        iconResId = R.drawable.ic_juice,
        titleResId = R.string.home_beverage_coffee_title,
        waterBalanceDelta = 50,
        color = Green
    ),
    COLA(
        type = Drink.COLA,
        iconResId = R.drawable.ic_cola,
        titleResId = R.string.home_beverage_cola_title,
        waterBalanceDelta = 75,
        color = PurpleLight
    ),
}