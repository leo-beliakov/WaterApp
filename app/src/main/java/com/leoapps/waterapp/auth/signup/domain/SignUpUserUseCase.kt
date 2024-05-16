package com.leoapps.waterapp.auth.signup.domain

import com.google.firebase.auth.FirebaseUser
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator suspend fun invoke(
        name: String,
        email: String,
        password: String
    ): Flow<TaskResult<FirebaseUser>> {
        return repository.signupUser(name, email, password)
    }
}