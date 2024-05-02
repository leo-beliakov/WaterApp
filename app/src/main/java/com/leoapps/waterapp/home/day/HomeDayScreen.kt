package com.leoapps.waterapp.home.day

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import com.leoapps.waterapp.home.day.composables.BeverageItem
import com.leoapps.waterapp.home.day.composables.progress.CircleProgress
import com.leoapps.waterapp.home.day.composables.progress.rememberProgressState
import com.leoapps.waterapp.home.day.model.HomeDayUiEffect


@Composable
fun HomeDayScreen(
    viewModel: HomeDayViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val circleProgressState = rememberProgressState(state.progressState.progress)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        CircleProgress(
            title = state.progressState.percentText,
            subtitle = state.progressState.consumedText,
            progressState = circleProgressState,
            modifier = Modifier.fillMaxWidth(0.7f),
        )
        LazyVerticalGrid(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            columns = GridCells.Fixed(2)
        ) {
            items(
                items = state.beverageItems,
                key = { it.name }
            ) { beverage ->
                BeverageItem(
                    beverageItem = beverage,
                    onClick = viewModel::onBeverageClick
                )
            }
        }
    }


    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            is HomeDayUiEffect.AnimateProgressTo -> {
                circleProgressState.animateProgressTo(effect.progress)
            }
        }
    }
}