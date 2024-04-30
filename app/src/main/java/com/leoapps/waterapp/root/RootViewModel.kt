package com.leoapps.waterapp.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.root.model.RootTab
import com.leoapps.waterapp.root.model.RootUiEffect
import com.leoapps.waterapp.root.model.RootUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(RootUiState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<RootUiEffect>(replay = 1)
    val sideEffects: SharedFlow<RootUiEffect> = _sideEffects

    init {
        _state
            .map { it.selectedTab }
            .onEach { selectedTab ->
                _sideEffects.emit(RootUiEffect.NavigateToTab(selectedTab))
            }.launchIn(viewModelScope)
    }

    fun onTabClicked(tab: RootTab) {
        _state.update { it.copy(selectedTab = tab) }
    }
}