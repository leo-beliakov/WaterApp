package com.leoapps.waterapp.profile.presentation.navigation

import androidx.navigation.NavController

class ProfileNavigator(
    private val navController: NavController
) {
    fun openAuthScreen() {
        navController.navigate("login")
    }
}