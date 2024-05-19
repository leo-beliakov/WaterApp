package com.leoapps.waterapp.water.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.firebase.firestore.FirebaseFirestore
import com.leoapps.waterapp.water.data.UserRepositoryFirebaseImpl
import com.leoapps.waterapp.water.data.WaterBalanceRepositoryLocalImpl
import com.leoapps.waterapp.water.domain.UserRepository
import com.leoapps.waterapp.water.domain.WaterBalanceRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("water-app-datastore")
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()
}

@Module
@InstallIn(SingletonComponent::class)
interface DataBindsModule {

    @Binds
    fun bindsRepository(impl: WaterBalanceRepositoryLocalImpl): WaterBalanceRepository

    @Singleton
    @Binds
    fun bindsFirebaseRepository(impl: UserRepositoryFirebaseImpl): UserRepository
}