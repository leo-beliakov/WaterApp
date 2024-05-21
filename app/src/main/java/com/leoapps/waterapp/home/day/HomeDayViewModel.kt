package com.leoapps.waterapp.home.day

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.auth.common.domain.GetCurrentUserAsFlowUseCase
import com.leoapps.waterapp.common.vibrator.domain.WaterAppVibrator
import com.leoapps.waterapp.home.day.model.DayProgressState
import com.leoapps.waterapp.home.day.model.DayUiState
import com.leoapps.waterapp.home.day.model.DrinkItem
import com.leoapps.waterapp.home.day.model.HomeDayUiEffect
import com.leoapps.waterapp.water.domain.GetUserWaterDataAsFlowUseCase
import com.leoapps.waterapp.water.domain.UpdateWaterGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeDayViewModel @Inject constructor(
    private val getCurrentUserAsFlow: GetCurrentUserAsFlowUseCase,
    private val getUserWaterDataAsFlow: GetUserWaterDataAsFlowUseCase,
    private val updateUserWaterData: UpdateWaterGoalUseCase,
    private val vibrator: WaterAppVibrator
) : ViewModel() {

    private val goalState = MutableStateFlow(getInitialGoalState())

    private val _state = MutableStateFlow(getInitialUiState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<HomeDayUiEffect>()
    val sideEffects: SharedFlow<HomeDayUiEffect> = _sideEffects

    private var currentUserId: String? = null

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

        getCurrentUserAsFlow()
            .distinctUntilChanged { old, new -> new?.id == old?.id }
            .flatMapLatest { user ->
                currentUserId = user?.id
                user?.id?.let { getUserWaterDataAsFlow(it) } ?: emptyFlow()
            }
            .onEach { waterData ->
                if (waterData != null) {
                    val goalPercent = waterData.goal
                    //todo calculate progress & update the state
                } else {
                    _state.update {
                        it.copy(
                            progressState = DayUiState.ProgressState(
                                percentText = "0%",
                                consumedText = "0 ml",
                                progress = 0f,
                            )
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun getInitialGoalState() = DayProgressState(
        goalMl = 1900
    )

    private fun getInitialUiState() = DayUiState(
        progressState = DayUiState.ProgressState(),
        drinkItems = DrinkItem.entries,
    )

    fun onBeverageClick(drinkItem: DrinkItem) {
        viewModelScope.launch {
            vibrator.vibrateOnClick()
            val updatedWaterBalance = goalState.value.consumedMl + drinkItem.waterBalanceDelta
//            updateUserWaterData()
//            repository.setWaterBalance(updatedWaterBalance)
        }
    }
}