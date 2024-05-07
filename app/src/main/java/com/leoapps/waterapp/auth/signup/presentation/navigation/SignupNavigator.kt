package com.leoapps.waterapp.auth.signup.presentation.navigation

import androidx.compose.ui.platform.UriHandler
import androidx.navigation.NavController

class SignupNavigator(
    private val navController: NavController,
    private val uriHandler: UriHandler
) {

    fun goBack() {
        navController.popBackStack()
    }

    fun closeAuth() {
        navController.popBackStack("login", true)
    }

    fun openUri(url: String) {
        uriHandler.openUri(url)
    }
}
