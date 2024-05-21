package com.leoapps.waterapp.water.domain

import javax.inject.Inject

class UpdateWaterGoalUseCase @Inject constructor(
    private val repository: WaterDataRepository
) {

    suspend operator fun invoke(userId: String, goal: Int) {
        return repository.updateWaterGoal(userId, goal)
    }
}