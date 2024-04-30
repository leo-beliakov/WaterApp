package com.leoapps.waterapp.common.utils

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

/**
 * Simplifies navigation between "tabs" in NavigationBar.
 * Avoids building excessive back stack of tabs
 * Avoids multiple copies of the same tab
 * Restores state of previously opened tab
 */
fun NavHostController.openTab(route: String) {
    navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destination
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}