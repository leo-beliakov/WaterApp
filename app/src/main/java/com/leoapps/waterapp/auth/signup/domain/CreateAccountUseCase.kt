package com.leoapps.waterapp.auth.signup.domain

import com.leoapps.waterapp.auth.common.domain.AuthRepository
import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator suspend fun invoke(
        name: String,
        email: String,
        password: String
    ): Flow<TaskResult<User>> {
        return authRepository.createAccount(name, email, password)
    }
}