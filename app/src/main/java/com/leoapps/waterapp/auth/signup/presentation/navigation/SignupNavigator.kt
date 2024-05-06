package com.leoapps.waterapp.auth.signup.presentation.navigation

import androidx.navigation.NavController

class SignupNavigator(
    private val navController: NavController
) {

    fun goBack() {
        navController.popBackStack()
    }

    fun closeAuth() {
        navController.popBackStack("login", true)
    }
}
