package com.leoapps.waterapp.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.R
import com.leoapps.waterapp.composables.tab_bar.TabBarTab
import com.leoapps.waterapp.root.model.RootSideEffect
import com.leoapps.waterapp.root.model.RootTab
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

    private val _state = MutableStateFlow(getInitialState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<RootSideEffect>(replay = 1)
    val sideEffects: SharedFlow<RootSideEffect> = _sideEffects

    init {
        _state
            .map { it.selectedTabId }
            .onEach { selectedTabId ->
                _sideEffects.emit(RootSideEffect.NavigateToTab(selectedTabId))
            }.launchIn(viewModelScope)
    }

    fun onTabClicked(tab: TabBarTab) {
        _state.update { it.copy(selectedTabId = tab.id) }
    }

    private fun getInitialState() = RootUiState(
        selectedTabId = RootTab.HOME.route,
        tabs = listOf(
            TabBarTab(
                id = RootTab.HOME.route,
                iconResId = R.drawable.ic_drop,
            ),
            TabBarTab(
                id = RootTab.BOTTLE.route,
                iconResId = R.drawable.ic_bottle,
            ),
            TabBarTab(
                id = RootTab.PROFILE.route,
                iconResId = R.drawable.ic_profile,
            )
        )
    )
}