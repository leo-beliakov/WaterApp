package com.leoapps.waterapp.auth.common.domain

import com.leoapps.waterapp.auth.common.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUserAsFlow(): Flow<User?>
    fun isAuthenticated(): Boolean
    suspend fun logoutUser()
    suspend fun signupUser(
        name: String,
        email: String,
        password: String,
    ): Boolean
}