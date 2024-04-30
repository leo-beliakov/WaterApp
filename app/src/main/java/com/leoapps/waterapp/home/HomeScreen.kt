package com.leoapps.waterapp.home

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
import com.leoapps.waterapp.home.day.HomeDayScreen
import com.leoapps.waterapp.home.model.HomeTab
import com.leoapps.waterapp.home.model.HomeUiEffect
import com.leoapps.waterapp.home.week.HomeWeekScreen

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = "day"
        ) {
            composable("day") {
                HomeDayScreen()
            }
            composable("week") {
                HomeWeekScreen()
            }
        }
        TabBar<HomeTab>(
            size = TabBarSize.MEDIUM,
            colorScheme = TabBarColorScheme.INVERTED,
            tabs = state.tabs,
            selectedTab = state.selectedTab,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    vertical = 22.dp,
                    horizontal = 36.dp
                ),
            onTabClicked = viewModel::onTabClicked
        )
    }

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            is HomeUiEffect.NavigateToTab -> {
                navController.openTab(effect.tab.route)
            }
        }
    }

}