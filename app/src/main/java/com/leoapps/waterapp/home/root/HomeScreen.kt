package com.leoapps.waterapp.home.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.composables.tab_bar.TabBar
import com.leoapps.waterapp.common.composables.tab_bar.TabBarColorScheme
import com.leoapps.waterapp.common.composables.tab_bar.TabBarSize
import com.leoapps.waterapp.common.presentation.theme.NavyDark
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import com.leoapps.waterapp.common.utils.openTab
import com.leoapps.waterapp.home.day.HomeDayScreen
import com.leoapps.waterapp.home.root.model.HomeTab
import com.leoapps.waterapp.home.root.model.HomeUiEffect
import com.leoapps.waterapp.home.week.HomeWeekScreen

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 36.dp)
    ) {
        Text(
            text = stringResource(id = R.string.home_screen_title),
            style = TextStyle.Default.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight(700),
                color = NavyDark
            ),
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(top = 22.dp)
        )
        TabBar<HomeTab>(
            size = TabBarSize.MEDIUM,
            colorScheme = TabBarColorScheme.INVERTED,
            tabs = state.tabs,
            selectedTab = state.selectedTab,
            onTabClicked = viewModel::onTabClicked
        )
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
    }

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            is HomeUiEffect.NavigateToTab -> {
                navController.openTab(effect.tab.route)
            }
        }
    }

}