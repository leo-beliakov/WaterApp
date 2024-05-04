package com.leoapps.waterapp.root.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leoapps.waterapp.auth.login.presentation.LoginScreen
import com.leoapps.waterapp.auth.login.presentation.navigation.LoginNavigator
import com.leoapps.waterapp.auth.signup.presentation.SignupScreen
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import com.leoapps.waterapp.main.MainScreen
import com.leoapps.waterapp.onboarding.presentation.OnboardingScreen
import com.leoapps.waterapp.root.presentation.navigator.RootNavigator
import com.leoapps.waterapp.splash.presentation.SplashScreen

@Composable
fun RootScreen(
    viewModel: RootViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val navigator = remember { RootNavigator(navController) }

    NavHost(
        navController = navController,
        startDestination = "login",
    ) {
        composable("splash") {
            SplashScreen()
        }
        composable("onboarding") {
            OnboardingScreen(
                onNextClicked = { navigator.openMain() }
            )
        }
        composable("main") {
            MainScreen(
                onLoginClicked = { navigator.openAuth() }
            )
        }
        composable("login") {
            LoginScreen(
                navigator = LoginNavigator(navController)
            )
        }
        composable("signup") {
            SignupScreen()
        }
    }

    CollectEventsWithLifecycle(viewModel.sideEffects) { effects ->
//        when (effects) {
//            RootUiEffect.GoToMain -> navigator.openMain()
//            RootUiEffect.GoToOnboarding -> navigator.openOnboarding()
//        }
    }
}