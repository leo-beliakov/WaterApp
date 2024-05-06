package com.leoapps.waterapp

import android.app.Application
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WaterApp : Application() {

    private val auth by lazy { Firebase.auth }

    override fun onCreate() {
        super.onCreate()
        auth.addAuthStateListener { authInfo ->
            Log.d("MyTag", "authInfo.currentUser = ${authInfo.currentUser}")
        }
    }
}