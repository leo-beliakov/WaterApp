package com.leoapps.waterapp.auth.common.di

import com.leoapps.waterapp.auth.common.data.FirebaseAuthRepository
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun bindsRepository(impl: FirebaseAuthRepository): AuthRepository
}