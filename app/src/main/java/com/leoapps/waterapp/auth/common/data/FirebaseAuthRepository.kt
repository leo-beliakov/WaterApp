package com.leoapps.waterapp.auth.common.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.leoapps.waterapp.auth.common.domain.AuthRepository
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor() : AuthRepository {

    private val auth by lazy { Firebase.auth }
    override fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }
}