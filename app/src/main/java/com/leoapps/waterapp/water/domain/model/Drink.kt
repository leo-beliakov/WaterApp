package com.leoapps.waterapp.water.domain.model

enum class Drink(val balanceImpact: Int) {
    WATER(250),
    COFFEE(-100),
    JUICE(50),
    COLA(75),
}