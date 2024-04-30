package com.leoapps.waterapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.home.model.HomeTab
import com.leoapps.waterapp.home.model.HomeUiEffect
import com.leoapps.waterapp.home.model.HomeUiState
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
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()


    private val _sideEffects = MutableSharedFlow<HomeUiEffect>(replay = 1)
    val sideEffects: SharedFlow<HomeUiEffect> = _sideEffects

    init {
        _state
            .map { it.selectedTab }
            .onEach { selectedTab ->
                _sideEffects.emit(HomeUiEffect.NavigateToTab(selectedTab))
            }.launchIn(viewModelScope)
    }

    fun onTabClicked(tab: HomeTab) {
        _state.update { it.copy(selectedTab = tab) }
    }
}