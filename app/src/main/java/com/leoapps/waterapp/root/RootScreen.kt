package com.leoapps.waterapp.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import com.leoapps.waterapp.common.utils.openTab
import com.leoapps.waterapp.composables.tab_bar.TabBar
import com.leoapps.waterapp.composables.tab_bar.TabBarColorScheme
import com.leoapps.waterapp.composables.tab_bar.TabBarSize
import com.leoapps.waterapp.home.HomeScreen
import com.leoapps.waterapp.root.model.RootTab
import com.leoapps.waterapp.root.model.RootUiEffect
import com.leoapps.waterapp.water.ProfileScreen
import com.leoapps.waterapp.water.WaterScreen

@Composable
fun RootScreen(
    viewModel: RootViewModel = hiltViewModel()
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
        TabBar<RootTab>(
            size = TabBarSize.LARGE,
            colorScheme = TabBarColorScheme.PRIMARY,
            tabs = state.tabs,
            selectedTab = state.selectedTab,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(22.dp),
            onTabClicked = viewModel::onTabClicked
        )
    }

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            is RootUiEffect.NavigateToTab -> {
                navController.openTab(effect.tab.route)
            }
        }
    }
}