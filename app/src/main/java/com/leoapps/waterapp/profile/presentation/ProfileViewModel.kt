package com.leoapps.waterapp.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.auth.common.domain.GetCurrentUserAsFlowUseCase
import com.leoapps.waterapp.auth.common.domain.LogoutUserUseCase
import com.leoapps.waterapp.profile.presentation.model.ProfileUiEffect
import com.leoapps.waterapp.profile.presentation.model.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserAsFlow: GetCurrentUserAsFlowUseCase,
    private val logOutUser: LogoutUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<ProfileUiEffect>()
    val sideEffects: SharedFlow<ProfileUiEffect> = _sideEffects

    init {
        getCurrentUserAsFlow()
            .onEach { user ->
                _state.update {
                    it.copy(
                        username = user?.name ?: "",
                        profileInfoVisible = user != null,
                        loginButtonVisible = user == null,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _sideEffects.emit(ProfileUiEffect.OpenLogin)
        }
    }

    fun onLogOutClicked() {
        viewModelScope.launch {
            logOutUser()
        }
    }
}