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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import com.leoapps.waterapp.composables.tab_bar.TabBar
import com.leoapps.waterapp.composables.tab_bar.TabBarColorScheme
import com.leoapps.waterapp.composables.tab_bar.TabBarSize
import com.leoapps.waterapp.home.HomeScreen
import com.leoapps.waterapp.root.model.RootSideEffect
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
        TabBar(
            size = TabBarSize.LARGE,
            colorScheme = TabBarColorScheme.PRIMARY,
            tabs = state.tabs,
            selectedTabId = state.selectedTabId,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(22.dp),
            onTabClicked = viewModel::onTabClicked
        )
    }

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            is RootSideEffect.NavigateToTab -> {
                navController.navigate(effect.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destination
                    // on the back stack as users select items
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            }
        }
    }
}