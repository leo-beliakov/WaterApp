package com.leoapps.waterapp.onboarding.di

import com.leoapps.waterapp.onboarding.data.OnboardingRepositoryImpl
import com.leoapps.waterapp.onboarding.domain.OnboardingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface OnboardingModule {

    @Binds
    fun bindsOnboardingRepository(
        impl: OnboardingRepositoryImpl
    ): OnboardingRepository
}