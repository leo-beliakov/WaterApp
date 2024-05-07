package com.leoapps.waterapp.auth.common.domain

import com.leoapps.waterapp.common.domain.task_result.TaskResult
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    operator suspend fun invoke(): TaskResult<Unit> {
        return repository.deleteUser()
    }
}