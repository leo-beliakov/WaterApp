package com.leoapps.waterapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WaterApp : Application() {
    override fun onCreate() {
        super.onCreate()

        //todo only for debug:
        Timber.plant(Timber.DebugTree())
    }
}