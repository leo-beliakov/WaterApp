package com.leoapps.waterapp.auth.signup.presentation

import androidx.compose.ui.focus.FocusState
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.login.LoginResult
import com.google.firebase.auth.FirebaseUser
import com.leoapps.waterapp.R
import com.leoapps.waterapp.auth.common.data.FacebookAuthHelper
import com.leoapps.waterapp.auth.common.data.GoogleAuthHelper
import com.leoapps.waterapp.auth.signup.domain.SignUpUserUseCase
import com.leoapps.waterapp.auth.signup.domain.ValidateEmailUseCase
import com.leoapps.waterapp.auth.signup.domain.ValidateNameUseCase
import com.leoapps.waterapp.auth.signup.domain.ValidatePasswordUseCase
import com.leoapps.waterapp.auth.signup.presentation.mapper.SignupMapper
import com.leoapps.waterapp.auth.signup.presentation.model.PasswordStrengthItemState
import com.leoapps.waterapp.auth.signup.presentation.model.SignUpUiEffect
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiState
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
class SignUpViewModel @Inject constructor(
    private val signUpUser: SignUpUserUseCase,
    private val validatePassword: ValidatePasswordUseCase,
    private val isNameValid: ValidateNameUseCase,
    private val isEmailValid: ValidateEmailUseCase,
    private val googleAuthHelper: GoogleAuthHelper,
    private val facebookAuthHelper: FacebookAuthHelper,
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
        if (state.value.createAccountButtonEnabled) {
            createAccount()
        }
    }

    fun onCreateClicked() {
        createAccount()
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _sideEffects.emit(SignUpUiEffect.NavigateBack)
        }
    }

    fun onTermsClicked() {
        viewModelScope.launch {
            _sideEffects.emit(SignUpUiEffect.OpenUrl(TERMS_AND_CONDITIONS_URL))
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

    fun onGoogleLoginClicked() {
        viewModelScope.launch {
            _sideEffects.emit(SignUpUiEffect.RequestGoogleAuth(googleAuthHelper.signInRequest))
        }
    }

    fun onFacebookLoginClicked() {
        viewModelScope.launch {
            _sideEffects.emit(SignUpUiEffect.RequestFacebookAuth)
        }
    }

    fun onGoogleAuthSuccess(result: GetCredentialResponse) = viewModelScope.launch {
        googleAuthHelper
            .handleSuccessSignIn(result)
            .collect(::handleAuthResult)
    }

    fun onFacebookAuthSuccess(result: LoginResult) = viewModelScope.launch {
        facebookAuthHelper
            .handleSuccessSignIn(result)
            .collect(::handleAuthResult)
    }

    fun onFailedSignInResponse(exception: Exception) {
        if (exception !is GetCredentialCancellationException) {
            viewModelScope.launch {
                _sideEffects.emit(SignUpUiEffect.ShowAuthFailed)
            }
        }
    }

    private fun createAccount() = viewModelScope.launch {
        signUpUser(
            name = _state.value.name,
            email = _state.value.email,
            password = _state.value.password,
        ).collect(::handleAuthResult)
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
        createAccountButtonEnabled = false
    )

    private fun updateCreateButtonState() {
        _state.update {
            it.copy(
                createAccountButtonEnabled = it.termsAccepted &&
                        it.name.isNotEmpty() &&
                        it.email.isNotEmpty() &&
                        isEmailValid(it.email) &&
                        it.password.isNotEmpty() &&
                        it.passwordStrengths.all {
                            it.checkResult == PasswordStrengthItemState.CheckResult.CHECK_SUCCED
                        }
            )
        }
    }

    private suspend fun handleAuthResult(authResult: TaskResult<FirebaseUser>) {
        when (authResult) {
            TaskResult.Loading -> {
                _state.update { it.copy(loading = true) }
            }

            is TaskResult.Failure -> {
                _state.update { it.copy(loading = true) }
                _sideEffects.emit(SignUpUiEffect.ShowAuthFailed)
            }

            is TaskResult.Success -> {
                _sideEffects.emit(SignUpUiEffect.NavigateCloseAuth)
            }
        }
    }

    companion object {
        private const val TERMS_AND_CONDITIONS_URL = "https://www.google.com"
    }
}