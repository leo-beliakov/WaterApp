package com.leoapps.waterapp.water.domain.model

import com.google.firebase.Timestamp

data class WaterRecord(
    val drink: Drink = Drink.WATER,
    val timestamp: Timestamp = Timestamp.now()
)