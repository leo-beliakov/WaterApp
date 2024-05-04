package com.leoapps.waterapp.auth.common.domain

interface AuthRepository {
    fun isAuthenticated(): Boolean
    suspend fun signupUser(email: String, password: String): Boolean
}