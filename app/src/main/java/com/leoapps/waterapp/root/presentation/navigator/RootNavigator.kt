package com.leoapps.waterapp.root.presentation.navigator

import androidx.navigation.NavController

class RootNavigator(
    private val navController: NavController
) {
    fun openMain() {
        navController.navigate("main") {
            popUpTo(navController.backQueue.first().destination.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun openOnboarding() {
        navController.navigate("onboarding") {
            popUpTo(navController.backQueue.first().destination.id) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun openAuth() {
        navController.navigate("login") {
            launchSingleTop = true
        }
    }

    fun openSignup() {
        navController.navigate("signup") {
            launchSingleTop = true
        }
    }
}