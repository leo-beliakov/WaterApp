package com.leoapps.waterapp.auth.login.presentation

import androidx.compose.ui.focus.FocusState
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.login.LoginResult
import com.leoapps.waterapp.auth.common.domain.LogInUseCase
import com.leoapps.waterapp.auth.common.domain.model.EmailCredential
import com.leoapps.waterapp.auth.common.domain.model.User
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiEffect
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiState
import com.leoapps.waterapp.auth.signup.domain.ValidateEmailUseCase
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val logIn: LogInUseCase,
    private val isEmailValid: ValidateEmailUseCase
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
        if (state.value.loginButtonEnabled) {
            loginWithEmail()
        }
    }

    fun onLoginButtonClicked() {
        if (state.value.loginButtonEnabled) {
            loginWithEmail()
        }
    }

    fun onSignupClicked() {
        viewModelScope.launch {
            _sideEffects.emit(LoginUiEffect.NavigateToSignUp)
        }
    }

    fun onGoogleLoginClicked() {
        viewModelScope.launch {
            _sideEffects.emit(LoginUiEffect.RequestGoogleAuth)
        }
    }

    fun onFacebookLoginClicked() {
        viewModelScope.launch {
            _sideEffects.emit(LoginUiEffect.RequestFacebookAuth)
        }
    }

    private fun loginWithEmail() = viewModelScope.launch {
        logIn(EmailCredential(_state.value.email, _state.value.password))
            .collect(::handleAuthResult)
    }

    fun onGoogleAuthSuccess(result: GetCredentialResponse) = viewModelScope.launch {
        logIn(result).collect(::handleAuthResult)
    }

    fun onFacebookAuthSuccess(result: LoginResult) = viewModelScope.launch {
        logIn(result).collect(::handleAuthResult)
    }

    fun onFailedSignInResponse(exception: Exception) {
        if (exception !is GetCredentialCancellationException) {
            viewModelScope.launch {
                _sideEffects.emit(LoginUiEffect.ShowAuthFailed)
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _sideEffects.emit(LoginUiEffect.NavigateClose)
        }
    }

    private fun updateLoginButtonState() {
        _state.update {
            it.copy(
                loginButtonEnabled = it.email.isNotEmpty() &&
                        isEmailValid(it.email) &&
                        it.password.isNotEmpty()
            )
        }
    }

    private suspend fun handleAuthResult(authResult: TaskResult<User>) {
        when (authResult) {
            TaskResult.Loading -> {
                _state.update { it.copy(loading = true) }
            }

            is TaskResult.Failure -> {
                _state.update { it.copy(loading = true) }
                _sideEffects.emit(LoginUiEffect.ShowAuthFailed)
            }

            is TaskResult.Success -> {
                _sideEffects.emit(LoginUiEffect.NavigateClose)
            }
        }
    }
}