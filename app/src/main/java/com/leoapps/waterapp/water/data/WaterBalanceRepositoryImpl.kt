package com.leoapps.waterapp.water.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.leoapps.waterapp.water.domain.WaterBalanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WaterBalanceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : WaterBalanceRepository {
    override fun getWaterBalanceAsFlow(): Flow<Int> {
        return dataStore.data.map { prefs ->
            prefs[WATER_BALANCE_KEY] ?: 0
        }
    }

    override suspend fun setWaterBalance(balance: Int) {
        dataStore.edit { prefs ->
            prefs[WATER_BALANCE_KEY] = balance
        }
    }

    private companion object {
        val WATER_BALANCE_KEY = intPreferencesKey("water-balance-key")
    }
}