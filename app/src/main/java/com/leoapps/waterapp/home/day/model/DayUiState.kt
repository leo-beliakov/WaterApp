package com.leoapps.waterapp.home.day.model

data class DayUiState(
    val progressState: ProgressState,
    val drinkItems: List<DrinkItem>
) {
    data class ProgressState(
        val percentText: String = "",
        val consumedText: String = "",
        val progress: Float = 0f
    )
}

data class DayProgressState(
    val goalMl: Int,
    val consumedMl: Int = 0
)