package com.leoapps.waterapp.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leoapps.waterapp.common.composables.tab_bar.TabBar
import com.leoapps.waterapp.common.composables.tab_bar.TabBarColorScheme
import com.leoapps.waterapp.common.composables.tab_bar.TabBarSize
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import com.leoapps.waterapp.common.utils.openTab
import com.leoapps.waterapp.home.root.HomeScreen
import com.leoapps.waterapp.main.model.MainTab
import com.leoapps.waterapp.main.model.MainUiEffect
import com.leoapps.waterapp.water.ProfileScreen
import com.leoapps.waterapp.water.WaterScreen

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen()
            }
            composable("bottle") {
                WaterScreen()
            }
            composable("profile") {
                ProfileScreen()
            }
        }
        TabBar<MainTab>(
            size = TabBarSize.LARGE,
            colorScheme = TabBarColorScheme.PRIMARY,
            tabs = state.tabs,
            selectedTab = state.selectedTab,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(22.dp),
            onTabClicked = viewModel::onTabClicked
        )
    }

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            is MainUiEffect.NavigateToTab -> {
                navController.openTab(effect.tab.route)
            }
        }
    }
}