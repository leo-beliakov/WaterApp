package com.leoapps.waterapp.water.domain

import com.leoapps.waterapp.water.domain.model.WaterData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserWaterDataAsFlowUseCase @Inject constructor(
    private val repository: WaterDataRepository
) {

    suspend operator fun invoke(userId: String): Flow<WaterData?> {
        return repository.getUserWaterDataAsFlow(userId)
    }
}