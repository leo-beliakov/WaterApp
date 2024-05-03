package com.leoapps.waterapp.onboarding.domain

interface OnboardingRepository {
    suspend fun isOnboardingShown(): Boolean
    suspend fun setOnboardingShown(isShown: Boolean)
}