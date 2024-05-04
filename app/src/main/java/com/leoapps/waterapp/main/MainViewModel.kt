package com.leoapps.waterapp.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.main.model.MainTab
import com.leoapps.waterapp.main.model.MainUiEffect
import com.leoapps.waterapp.main.model.MainUiState
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
class MainViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(MainUiState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<MainUiEffect>()
    val sideEffects: SharedFlow<MainUiEffect> = _sideEffects

    init {
        _state
            .map { it.selectedTab }
            .onEach { selectedTab ->
                _sideEffects.emit(MainUiEffect.NavigateToTab(selectedTab))
            }.launchIn(viewModelScope)
    }

    fun onTabClicked(tab: MainTab) {
        _state.update { it.copy(selectedTab = tab) }
    }
}