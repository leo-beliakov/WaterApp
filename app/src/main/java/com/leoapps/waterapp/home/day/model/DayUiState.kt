package com.leoapps.waterapp.home.day.model

class DayUiState(
    val progressState: ProgressState,
    val beverageItems: List<BeverageItem>
) {
    data class ProgressState(
        val percentText: String,
        val goalText: String,
        val progress: Float
    )
}