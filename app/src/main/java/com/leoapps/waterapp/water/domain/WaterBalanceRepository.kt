package com.leoapps.waterapp.water.domain

import com.leoapps.waterapp.auth.common.domain.model.User
import kotlinx.coroutines.flow.Flow

interface WaterBalanceRepository {
    fun getWaterBalanceAsFlow(): Flow<Int>
    suspend fun setWaterBalance(balance: Int)
}

interface UserRepository {
    fun getCurrentUserAsFlow(): Flow<User?>
    suspend fun deleteUser()
}