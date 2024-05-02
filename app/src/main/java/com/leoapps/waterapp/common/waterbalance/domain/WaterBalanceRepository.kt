package com.leoapps.waterapp.common.waterbalance.domain

import kotlinx.coroutines.flow.Flow

interface WaterBalanceRepository {
    fun getWaterBalanceAsFlow(): Flow<Int>
    suspend fun setWaterBalance(balance: Int)
}