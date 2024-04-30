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
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.leoapps.waterapp.R
import com.leoapps.waterapp.composables.BottomNavBarTab
import com.leoapps.waterapp.composables.ColorScheme
import com.leoapps.waterapp.composables.NavBarSize
import com.leoapps.waterapp.composables.NavBarTab
import com.leoapps.waterapp.composables.ToggleNavBar
import com.leoapps.waterapp.home.HomeScreen
import com.leoapps.waterapp.water.ProfileScreen
import com.leoapps.waterapp.water.WaterScreen

@Composable
fun RootScreen(
    viewModel: RootViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

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
        ToggleNavBar(
            size = NavBarSize.LARGE,
            colorScheme = ColorScheme.PRIMARY,
            tabs = listOf(
                NavBarTab(
                    id = BottomNavBarTab.HOME,
                    iconResId = R.drawable.ic_drop,
                    isSelected = true
                ),
                NavBarTab(
                    id = BottomNavBarTab.BOTTLE,
                    iconResId = R.drawable.ic_bottle,
                    isSelected = false
                ),
                NavBarTab(
                    id = BottomNavBarTab.PROFILE,
                    iconResId = R.drawable.ic_profile,
                    isSelected = false
                ),
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(22.dp),
            onTabClicked = {
                val route = when (it.id) {
                    is BottomNavBarTab -> it.id.route
                    else -> null
                }

                route?.let {
                    navController.navigate(route) {
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
        )
    }
}