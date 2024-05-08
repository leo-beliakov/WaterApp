package com.leoapps.waterapp.auth.login.presentation

import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiEffect
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiState
import com.leoapps.waterapp.auth.signup.domain.SignInUserUseCase
import com.leoapps.waterapp.auth.signup.domain.ValidateEmailUseCase
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signinUser: SignInUserUseCase,
    private val isEmailValid: ValidateEmailUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<LoginUiEffect>()
    val sideEffects: SharedFlow<LoginUiEffect> = _sideEffects

    fun onPasswordUpdated(value: String) {
        _state.update { it.copy(password = value) }
        updateLoginButtonState()
    }

    fun onEmailUpdated(value: String) {
        _state.update {
            //We want to dismiss the error when the user has corrected the email,
            //but we don't want to raise the error while the user is typing
            val updatedErrorState = if (isEmailValid(value)) {
                false
            } else {
                it.showEmailInvalidError
            }

            it.copy(
                email = value,
                showEmailInvalidError = updatedErrorState
            )
        }
        updateLoginButtonState()
    }

    fun onEmailFocusChanged(focusState: FocusState) {
        if (!focusState.isFocused && state.value.email.isNotEmpty()) {
            _state.update {
                it.copy(
                    showEmailInvalidError = !isEmailValid(it.email)
                )
            }
        }
    }

    fun onDoneActionClicked() {
        if (state.value.buttonState.isEnabled) {
            loginUser()
        }
    }

    fun onLoginButtonClicked() {
        if (state.value.buttonState.isEnabled) {
            loginUser()
        }
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

    private fun loginUser() = viewModelScope.launch {
        signinUser(
            _state.value.email,
            _state.value.password,
        ).collectLatest { signinResult ->
            when (signinResult) {
                TaskResult.Loading -> {
                    setButtonLoading(true)
                }

                is TaskResult.Failure -> {
                    setButtonLoading(false)
                }

                is TaskResult.Success -> {
                    setButtonLoading(false)
                    _sideEffects.emit(LoginUiEffect.CloseAuth)
                }
            }
        }
    }

    private fun updateLoginButtonState() {
        _state.update {
            it.copy(
                buttonState = it.buttonState.copy(
                    isEnabled = it.email.isNotEmpty() &&
                            isEmailValid(it.email) &&
                            it.password.isNotEmpty()
                )
            )
        }
    }

    private fun setButtonLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                buttonState = it.buttonState.copy(
                    isLoading = isLoading
                )
            )
        }
    }
}