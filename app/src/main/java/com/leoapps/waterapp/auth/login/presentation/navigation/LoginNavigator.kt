package com.leoapps.waterapp.auth.login.presentation.navigation

import androidx.navigation.NavController

class LoginNavigator(
    private val navController: NavController
) {
    fun openSignup() {
        navController.navigate("signup") {
            launchSingleTop = true
        }
    }

    fun openGoBack() {

    }
}