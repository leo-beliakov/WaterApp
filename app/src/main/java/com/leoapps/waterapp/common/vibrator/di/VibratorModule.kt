package com.leoapps.waterapp.common.vibrator.di

import com.leoapps.waterapp.common.vibrator.data.VibratorManagerImpl
import com.leoapps.waterapp.common.vibrator.domain.WaterAppVibrator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface VibratorModule {
    @Binds
    fun bindsVibrator(impl: VibratorManagerImpl): WaterAppVibrator
}