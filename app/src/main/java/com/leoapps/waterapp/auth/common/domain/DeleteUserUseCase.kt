package com.leoapps.waterapp.auth.common.domain

import com.leoapps.waterapp.common.domain.task_result.TaskResult
import com.leoapps.waterapp.water.domain.WaterDataRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: AuthRepository,
    private val waterRepository: WaterDataRepository
) {

    operator suspend fun invoke(): Flow<TaskResult<Unit>> {
        return repository.deleteUser()
    }
}