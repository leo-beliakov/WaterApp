package com.leoapps.waterapp.common.domain

import kotlinx.coroutines.flow.Flow

interface WaterBalanceRepository {
    fun getWaterBalanceAsFlow(): Flow<Int>
    suspend fun setWaterBalance(balance: Int)
}