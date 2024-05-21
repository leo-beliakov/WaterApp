package com.leoapps.waterapp.water.domain.model

data class WaterData(
    val goal: Int = 0,
    val records: List<WaterRecord> = emptyList()
)