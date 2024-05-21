package com.leoapps.waterapp.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.auth.common.domain.GetCurrentUserAsFlowUseCase
import com.leoapps.waterapp.water.domain.GetUserWaterDataAsFlowUseCase
import com.leoapps.waterapp.water.domain.UpdateWaterGoalUseCase
import com.leoapps.waterapp.water.domain.model.WaterData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WaterViewModel @Inject constructor(
    private val getCurrentUserAsFlow: GetCurrentUserAsFlowUseCase,
    private val getUserWaterDataAsFlow: GetUserWaterDataAsFlowUseCase,
    private val updateUserWaterData: UpdateWaterGoalUseCase,
) : ViewModel() {

    private val _waterData = MutableStateFlow<WaterData?>(null)
    val waterData: StateFlow<WaterData?> get() = _waterData

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    private var currentUserId: String? = null

    init {
        getCurrentUserAsFlow()
            .distinctUntilChanged { old, new -> new?.id == old?.id }
            .flatMapLatest { user ->
                currentUserId = user?.id
                user?.id?.let { getUserWaterDataAsFlow(it) } ?: emptyFlow()
            }
            .onEach { _waterData.value = it }
            .launchIn(viewModelScope)
    }

    fun setWaterGoal(goal: Int) {
        viewModelScope.launch {
            try {
//                val updatedData = _waterData.value?.copy(
//                    goal = WaterGoal(goal)
//                )
                if (currentUserId != null) {
                    updateUserWaterData(currentUserId!!, goal)
                }
            } catch (exception: Exception) {
                _error.value = exception.message
            }
        }
    }
}
