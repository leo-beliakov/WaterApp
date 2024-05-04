package com.leoapps.waterapp.auth.signup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.auth.signup.domain.SignupUserUseCase
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiEffect
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUser: SignupUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignupUiState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<SignupUiEffect>()
    val sideEffects: SharedFlow<SignupUiEffect> = _sideEffects

    fun onNameUpdated(value: String) {
        _state.update { it.copy(name = value) }
    }

    fun onEmailUpdated(value: String) {
        _state.update { it.copy(email = value) }
    }

    fun onPasswordUpdated(value: String) {
        _state.update { it.copy(password = value) }
    }

    fun onTermsChecked(checked: Boolean) {
        _state.update { it.copy(termsAccepted = checked) }
    }

    fun onCreateClicked() {

    }

    fun onDoneActionClicked() {

    }

    fun onBackClicked() {
        viewModelScope.launch {
            _sideEffects.emit(SignupUiEffect.GoBack)
        }
    }

    fun onTermsClicked() {

    }
}