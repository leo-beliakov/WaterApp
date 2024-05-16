package com.leoapps.waterapp.auth.common.domain

import com.google.firebase.auth.FirebaseUser
import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUserAsFlow(): Flow<User?>
    suspend fun deleteUser(): Flow<TaskResult<Unit>>
    suspend fun logoutUser()
    suspend fun signinUser(
        email: String,
        password: String,
    ): Flow<TaskResult<FirebaseUser>>

    fun signupUser(
        name: String,
        email: String,
        password: String,
    ): Flow<TaskResult<FirebaseUser>>
}