package com.leoapps.waterapp.home.day

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.home.day.model.BeverageItem
import com.leoapps.waterapp.home.day.model.DayProgressState
import com.leoapps.waterapp.home.day.model.DayUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeDayViewModel @Inject constructor() : ViewModel() {

    private val goalState = MutableStateFlow(getInitialGoalState())

    private val _state = MutableStateFlow(getInitialUiState())
    val state = _state.asStateFlow()

    init {
        goalState
            .onEach { newDayState ->
                _state.update {
                    val goalPercent = newDayState.consumedMl.toFloat() / newDayState.goalMl
                    it.copy(
                        progressState = it.progressState.copy(
                            progress = goalPercent,
                            percentText = "${(goalPercent * 100).roundToInt()}%",
                            consumedText = "${newDayState.consumedMl} ml"
                        )
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun getInitialGoalState() = DayProgressState(
        goalMl = 1900
    )

    private fun getInitialUiState() = DayUiState(
        progressState = DayUiState.ProgressState(),
        beverageItems = BeverageItem.entries,
    )

    fun onBeverageClick(beverageItem: BeverageItem) {
        goalState.update {
            it.copy(consumedMl = it.consumedMl + beverageItem.waterBalanceDelta)
        }
    }
}