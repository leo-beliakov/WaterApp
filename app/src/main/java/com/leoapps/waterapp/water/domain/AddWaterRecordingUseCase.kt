package com.leoapps.waterapp.water.domain

import com.leoapps.waterapp.water.domain.model.Drink
import javax.inject.Inject

class AddWaterRecordingUseCase @Inject constructor(
    private val repository: WaterDataRepository
) {

    suspend operator fun invoke(userId: String, drink: Drink) {
        return repository.addWaterRecording(userId, drink)
    }
}