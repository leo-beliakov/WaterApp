package com.leoapps.waterapp.auth.signup.domain

import com.leoapps.waterapp.auth.common.domain.AuthRepository
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import javax.inject.Inject

class SignUpUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator suspend fun invoke(
        name: String,
        email: String,
        password: String
    ): TaskResult<Unit> {
        return repository.signupUser(name, email, password)
    }
}