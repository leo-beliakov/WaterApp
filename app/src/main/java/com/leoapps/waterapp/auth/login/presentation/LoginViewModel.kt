package com.leoapps.waterapp.auth.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiEffect
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<LoginUiEffect>()
    val sideEffects: SharedFlow<LoginUiEffect> = _sideEffects

    fun onPasswordUpdated(value: String) {
        _state.update { it.copy(password = value) }
    }

    fun onEmailUpdated(value: String) {
        _state.update { it.copy(email = value) }
    }

    fun onDoneActionClicked() {

    }

    fun onLoginButtonClicked() {

    }

    fun onSignupClicked() {
        viewModelScope.launch {
            _sideEffects.emit(LoginUiEffect.OpenSignUp)
        }
    }

    fun onGoogleLoginClicked() {

    }

    fun onFacebookLoginClicked() {

    }
}