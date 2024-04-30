package com.leoapps.waterapp.home.day

import androidx.lifecycle.ViewModel
import com.leoapps.waterapp.home.day.model.BeverageItem
import com.leoapps.waterapp.home.day.model.DayUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeDayViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(getInitialUiState())
    val state = _state.asStateFlow()

    private fun getInitialUiState() = DayUiState(
        progressState = DayUiState.ProgressState(
            percentText = "40%",
            goalText = "1235 ml",
            progress = 0.4f
        ),
        beverageItems = BeverageItem.entries,
    )

    fun onBeverageClick(beverageItem: BeverageItem) {

    }
}