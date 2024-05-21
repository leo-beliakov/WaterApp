package com.leoapps.waterapp.water.domain

import com.leoapps.waterapp.water.domain.model.Drink
import com.leoapps.waterapp.water.domain.model.WaterData
import kotlinx.coroutines.flow.Flow

interface WaterDataRepository {
    suspend fun getUserWaterDataAsFlow(userId: String): Flow<WaterData?>
    suspend fun deleteDataForUser(userId: String)
    suspend fun updateWaterGoal(userId: String, goal: Int)
    suspend fun addWaterRecording(userId: String, drink: Drink)
}