package com.leoapps.waterapp.auth.common.domain

import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUserAsFlow(): Flow<User?>
    suspend fun deleteUser(): TaskResult<Unit>
    suspend fun logoutUser()
    suspend fun signinUser(
        email: String,
        password: String,
    ): TaskResult<Unit>
    suspend fun signupUser(
        name: String,
        email: String,
        password: String,
    ): TaskResult<Unit>
}