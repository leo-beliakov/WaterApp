package com.leoapps.waterapp.auth.signup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.R
import com.leoapps.waterapp.auth.signup.domain.SignupUserUseCase
import com.leoapps.waterapp.auth.signup.domain.ValidateEmailUseCase
import com.leoapps.waterapp.auth.signup.domain.ValidatePasswordUseCase
import com.leoapps.waterapp.auth.signup.presentation.mapper.SignupMapper
import com.leoapps.waterapp.auth.signup.presentation.model.PasswordStrengthItemState
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiEffect
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiState
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButtonState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUser: SignupUserUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val isEmailValid: ValidateEmailUseCase,
    private val mapper: SignupMapper
) : ViewModel() {

    private val _state = MutableStateFlow(getInitialState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<SignupUiEffect>()
    val sideEffects: SharedFlow<SignupUiEffect> = _sideEffects

    fun onNameUpdated(value: String) {
        _state.update { it.copy(name = value) }
        updateCreateButtonState()
    }

    fun onEmailUpdated(value: String) {
        _state.update { it.copy(email = value) }
        updateCreateButtonState()
    }

    fun onPasswordUpdated(value: String) {
        _state.update {
            it.copy(
                password = value,
                passwordStrengths = mapper.mapPasswordValidationResult(
                    result = validatePassword(value)
                )
            )
        }
        updateCreateButtonState()
    }

    fun onTermsChecked(checked: Boolean) {
        _state.update { it.copy(termsAccepted = checked) }
        updateCreateButtonState()
    }

    fun onDoneActionClicked() {
        if (state.value.buttonState.isEnabled) {
            createAccount()
        }
    }

    fun onCreateClicked() {
        createAccount()
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _sideEffects.emit(SignupUiEffect.GoBack)
        }
    }

    fun onTermsClicked() {

    }

    private fun createAccount() {
        //todo add validation
        viewModelScope.launch {
            _state.update {
                it.copy(
                    buttonState = it.buttonState.copy(
                        isLoading = true
                    )
                )
            }

            //todo remove
            delay(600)

            signupUser(
                _state.value.name,
                _state.value.email,
                _state.value.password,
            )

            _state.update {
                it.copy(
                    buttonState = it.buttonState.copy(
                        isLoading = false
                    )
                )
            }
        }
    }

    private fun getInitialState() = SignupUiState(
        name = "",
        email = "",
        password = "",
        passwordStrengths = listOf(
            PasswordStrengthItemState(
                textResId = R.string.password_validation_digits,
                checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
            ),
            PasswordStrengthItemState(
                textResId = R.string.password_validation_lowercase,
                checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
            ),
            PasswordStrengthItemState(
                textResId = R.string.password_validation_uppercase,
                checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
            ),
            PasswordStrengthItemState(
                textResId = R.string.password_validation_special_caracter,
                checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
            ),
            PasswordStrengthItemState(
                textResId = R.string.password_validation_white_spaces,
                checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
            ),
            PasswordStrengthItemState(
                textResId = R.string.password_validation_length,
                checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
            ),
        ),
        termsAccepted = false,
        buttonState = ProgressButtonState(
            isEnabled = false,
            isLoading = false,
            textResId = R.string.sugnup_button_create,
        )
    )

    private fun updateCreateButtonState() {
        _state.update {
            it.copy(
                buttonState = it.buttonState.copy(
                    isEnabled = it.termsAccepted &&
                            it.name.isNotEmpty() &&
                            it.email.isNotEmpty() &&
                            isEmailValid(it.email) &&
                            it.password.isNotEmpty() &&
                            it.passwordStrengths.all {
                                it.checkResult == PasswordStrengthItemState.CheckResult.CHECK_SUCCED
                            }
                )
            )
        }
    }
}