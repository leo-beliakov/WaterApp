package com.leoapps.waterapp.auth.signup.domain

import com.leoapps.waterapp.auth.common.domain.AuthRepository
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator suspend fun invoke(
        email: String,
        password: String
    ): Flow<TaskResult<Unit>> {
        return repository.signinUser(email, password)
    }
}