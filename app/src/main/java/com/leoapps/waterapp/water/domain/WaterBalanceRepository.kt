package com.leoapps.waterapp.water.domain

import kotlinx.coroutines.flow.Flow

interface WaterBalanceRepository {
    fun getWaterBalanceAsFlow(): Flow<Int>
    suspend fun setWaterBalance(balance: Int)
}