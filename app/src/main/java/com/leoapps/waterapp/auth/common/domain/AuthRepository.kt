package com.leoapps.waterapp.auth.common.domain

interface AuthRepository {
    fun isAuthenticated(): Boolean
    suspend fun signupUser(
        name: String,
        email: String,
        password: String,
    ): Boolean
}