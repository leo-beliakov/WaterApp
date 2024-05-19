package com.leoapps.waterapp.auth.common.domain

import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUserAsFlow(): Flow<User?>
    suspend fun deleteUser(): Flow<TaskResult<Unit>>
    suspend fun logOut()
    fun createAccount(
        name: String,
        email: String,
        password: String,
    ): Flow<TaskResult<User>>

    fun logIn(
        credentials: Any
    ): Flow<TaskResult<User>>
}