package com.leoapps.waterapp.home.day

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.auth.common.domain.GetCurrentUserAsFlowUseCase
import com.leoapps.waterapp.common.vibrator.domain.WaterAppVibrator
import com.leoapps.waterapp.home.day.model.DayUiState
import com.leoapps.waterapp.home.day.model.DrinkItem
import com.leoapps.waterapp.home.day.model.HomeDayUiEffect
import com.leoapps.waterapp.water.domain.AddWaterRecordingUseCase
import com.leoapps.waterapp.water.domain.CalculateDayProgressUseCase
import com.leoapps.waterapp.water.domain.GetUserWaterDataAsFlowUseCase
import com.leoapps.waterapp.water.domain.model.WaterData
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
    private val calculateDayProgress: CalculateDayProgressUseCase,
    private val addWaterRecording: AddWaterRecordingUseCase,
    private val vibrator: WaterAppVibrator
) : ViewModel() {

    private val _state = MutableStateFlow(getInitialUiState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<HomeDayUiEffect>()
    val sideEffects: SharedFlow<HomeDayUiEffect> = _sideEffects

    private var currentUserId: String? = null

    init {
        getCurrentUserAsFlow()
            .distinctUntilChanged { old, new -> new?.id == old?.id }
            .flatMapLatest { user ->
                currentUserId = user?.id
                user?.id?.let { getUserWaterDataAsFlow(it) } ?: emptyFlow()
            }
            .onEach(::onWaterDataReceived)
            .launchIn(viewModelScope)
    }

    fun onDrinkClick(drinkItem: DrinkItem) {
        viewModelScope.launch {
            vibrator.vibrateOnClick()
            currentUserId?.let { addWaterRecording(it, drinkItem.type) }
        }
    }

    private suspend fun onWaterDataReceived(waterData: WaterData?) {
        if (waterData != null) {
            val progress = calculateDayProgress(waterData)
            _sideEffects.emit(HomeDayUiEffect.AnimateProgressTo(progress.progress))
            _state.update {
                it.copy(
                    progressState = DayUiState.ProgressState(
                        percentText = "${(progress.progress * 100).roundToInt()}%",
                        consumedText = "${progress.balanceMl} ml",
                        progress = progress.progress,
                    )
                )
            }
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

    private fun getInitialUiState() = DayUiState(
        progressState = DayUiState.ProgressState(),
        drinkItems = DrinkItem.entries,
    )
}