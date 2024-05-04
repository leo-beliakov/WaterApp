package com.leoapps.waterapp.auth.common.domain

interface AuthRepository {
    fun isAuthenticated(): Boolean
}