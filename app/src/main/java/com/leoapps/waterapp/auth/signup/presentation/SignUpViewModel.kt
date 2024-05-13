package com.leoapps.waterapp.auth.signup.presentation

import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leoapps.waterapp.R
import com.leoapps.waterapp.auth.signup.domain.SignUpUserUseCase
import com.leoapps.waterapp.auth.signup.domain.ValidateEmailUseCase
import com.leoapps.waterapp.auth.signup.domain.ValidateNameUseCase
import com.leoapps.waterapp.auth.signup.domain.ValidatePasswordUseCase
import com.leoapps.waterapp.auth.signup.presentation.mapper.SignupMapper
import com.leoapps.waterapp.auth.signup.presentation.model.PasswordStrengthItemState
import com.leoapps.waterapp.auth.signup.presentation.model.SignUpUiEffect
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiState
import com.leoapps.waterapp.common.domain.task_result.TaskResult
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButtonState
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
class SignUpViewModel @Inject constructor(
    private val signUpUser: SignUpUserUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val isNameValid: ValidateNameUseCase,
    private val isEmailValid: ValidateEmailUseCase,
    private val mapper: SignupMapper
) : ViewModel() {

    private val _state = MutableStateFlow(getInitialState())
    val state = _state.asStateFlow()

    private val _sideEffects = MutableSharedFlow<SignUpUiEffect>()
    val sideEffects: SharedFlow<SignUpUiEffect> = _sideEffects

    fun onNameUpdated(value: String) {
        _state.update {
            //We want to dismiss the error when the user has corrected the name,
            //but we don't want to raise the error while the user is typing
            val updatedErrorState = if (isNameValid(value)) {
                false
            } else {
                it.showNameInvalidError
            }

            it.copy(
                name = value,
                showNameInvalidError = updatedErrorState
            )
        }
        updateCreateButtonState()
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
            _sideEffects.emit(SignUpUiEffect.GoBack)
        }
    }

    fun onTermsClicked() {
        viewModelScope.launch {
            _sideEffects.emit(SignUpUiEffect.OpenUrl(TERMS_AND_CONDITIONS_URL))
        }
    }

    private fun createAccount() = viewModelScope.launch {
        signUpUser(
            _state.value.name,
            _state.value.email,
            _state.value.password,
        ).collectLatest { signupResult ->
            when (signupResult) {
                TaskResult.Loading -> {
                    setButtonLoading(true)
                }

                is TaskResult.Failure -> {
                    setButtonLoading(false)
                }

                is TaskResult.Success -> {
                    setButtonLoading(false)
                    _sideEffects.emit(SignUpUiEffect.CloseAuth)
                }
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
            textResId = R.string.signup_button_create,
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

    private fun setButtonLoading(isLoading: Boolean) {
        _state.update {
            it.copy(
                buttonState = it.buttonState.copy(
                    isLoading = isLoading
                )
            )
        }
    }

    fun onNameFocusChanged(focusState: FocusState) {
        if (!focusState.isFocused && state.value.name.isNotEmpty()) {
            _state.update {
                it.copy(
                    showNameInvalidError = isNameValid(it.name)
                )
            }
        }
    }

    fun onEmailFocusChanged(focusState: FocusState) {
        if (!focusState.isFocused && state.value.email.isNotEmpty()) {
            _state.update {
                it.copy(
                    showNameInvalidError = isEmailValid(it.email)
                )
            }
        }
    }

    companion object {
        private const val TERMS_AND_CONDITIONS_URL = "https://www.google.com"
    }
}