package com.leoapps.waterapp.auth.common.domain

import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(credentials: Any): Flow<TaskResult<User>> {
        return authRepository.logIn(credentials)
    }
}