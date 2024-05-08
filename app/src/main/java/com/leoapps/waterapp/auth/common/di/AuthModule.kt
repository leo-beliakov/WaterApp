package com.leoapps.waterapp.auth.common.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leoapps.waterapp.auth.common.data.FirebaseAuthRepository
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun bindsRepository(impl: FirebaseAuthRepository): AuthRepository

    companion object {

        @Singleton
        @Provides
        fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth
    }
}