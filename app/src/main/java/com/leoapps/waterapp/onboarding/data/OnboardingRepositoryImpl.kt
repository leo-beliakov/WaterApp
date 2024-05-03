package com.leoapps.waterapp.onboarding.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.leoapps.waterapp.onboarding.domain.OnboardingRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnboardingRepository {
    override suspend fun isOnboardingShown(): Boolean {
        val prefs = dataStore.data.firstOrNull() ?: return false
        return prefs[ONBOARDING_SHOWN_KEY] ?: false
    }

    override suspend fun setOnboardingShown(isShown: Boolean) {
        dataStore.edit { prefs ->
            prefs[ONBOARDING_SHOWN_KEY] = isShown
        }
    }

    private companion object {
        val ONBOARDING_SHOWN_KEY = booleanPreferencesKey("onboarding-shown-key")
    }
}