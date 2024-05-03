package com.leoapps.waterapp.root.presentation.navigator

import androidx.navigation.NavController

class RootNavigator(
    private val navController: NavController
) {
    fun openMain() {
        navController.navigate("main") {
            popUpTo("onboarding") { inclusive = true }
            launchSingleTop = true
        }
    }

    fun openOnboarding() {
        navController.navigate("onboarding") {
            popUpTo("splash") { inclusive = true }
            launchSingleTop = true
        }
    }
}