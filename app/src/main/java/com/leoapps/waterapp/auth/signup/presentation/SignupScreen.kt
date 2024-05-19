package com.leoapps.waterapp.auth.signup.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leoapps.waterapp.R
import com.leoapps.waterapp.auth.common.presentation.composables.FederatedIdentityButtons
import com.leoapps.waterapp.auth.common.presentation.rememberFacebookAuthHelper
import com.leoapps.waterapp.auth.common.presentation.rememberGoogleAuthHelper
import com.leoapps.waterapp.auth.signup.presentation.composables.PasswordStrengthItem
import com.leoapps.waterapp.auth.signup.presentation.composables.TermsAndConditionsCheckBox
import com.leoapps.waterapp.auth.signup.presentation.model.PasswordStrengthItemState
import com.leoapps.waterapp.auth.signup.presentation.model.SignUpUiEffect
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiState
import com.leoapps.waterapp.auth.signup.presentation.navigation.SignupNavigator
import com.leoapps.waterapp.common.presentation.composables.loading_fullscreen.LoadingFullScreen
import com.leoapps.waterapp.common.presentation.composables.toolbar.Toolbar
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigator: SignupNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val googleAuthHelper = rememberGoogleAuthHelper()
    val facebookAuthHelper = rememberFacebookAuthHelper()

    SignupScreen(
        state = state,
        onNameUpdated = viewModel::onNameUpdated,
        onEmailUpdated = viewModel::onEmailUpdated,
        onPasswordUpdated = viewModel::onPasswordUpdated,
        onNameFocusChanged = viewModel::onNameFocusChanged,
        onEmailFocusChanged = viewModel::onEmailFocusChanged,
        onTermsChecked = viewModel::onTermsChecked,
        onTermsClicked = viewModel::onTermsClicked,
        onCreateClicked = viewModel::onCreateClicked,
        onDoneActionClicked = viewModel::onDoneActionClicked,
        onGoogleLoginClicked = viewModel::onGoogleLoginClicked,
        onFacebookLoginClicked = viewModel::onFacebookLoginClicked,
        onBackClicked = viewModel::onBackClicked,
    )

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            SignUpUiEffect.NavigateBack -> navigator.goBack()
            SignUpUiEffect.NavigateCloseAuth -> navigator.closeAuth()
            is SignUpUiEffect.OpenUrl -> navigator.openUri(effect.url)

            SignUpUiEffect.ShowAuthFailed -> {
                Log.d("MyTag", "Failure to sign in")
            }

            SignUpUiEffect.RequestFacebookAuth -> {
                facebookAuthHelper.auth(
                    onSuccess = viewModel::onFacebookAuthSuccess,
                    onFailure = viewModel::onFailedSignInResponse,
                )
            }

            is SignUpUiEffect.RequestGoogleAuth -> {
                //For some reason, without switching to this scope we are getting
                //"Sign-in request cancelled by WaterApp" toast on UI
                scope.launch {
                    googleAuthHelper.auth(
                        onSuccess = viewModel::onGoogleAuthSuccess,
                        onFailure = viewModel::onFailedSignInResponse,
                    )
                }
            }
        }
    }
}

@Composable
private fun SignupScreen(
    state: SignupUiState,
    onNameUpdated: (String) -> Unit,
    onEmailUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onNameFocusChanged: (FocusState) -> Unit,
    onEmailFocusChanged: (FocusState) -> Unit,
    onTermsChecked: (Boolean) -> Unit,
    onTermsClicked: () -> Unit,
    onCreateClicked: () -> Unit,
    onDoneActionClicked: () -> Unit,
    onGoogleLoginClicked: () -> Unit,
    onFacebookLoginClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        SignupScreenContent(
            state = state,
            onNameUpdated = onNameUpdated,
            onEmailUpdated = onEmailUpdated,
            onPasswordUpdated = onPasswordUpdated,
            onNameFocusChanged = onNameFocusChanged,
            onEmailFocusChanged = onEmailFocusChanged,
            onTermsChecked = onTermsChecked,
            onTermsClicked = onTermsClicked,
            onCreateClicked = onCreateClicked,
            onDoneActionClicked = onDoneActionClicked,
            onGoogleLoginClicked = onGoogleLoginClicked,
            onFacebookLoginClicked = onFacebookLoginClicked,
            onBackClicked = onBackClicked,
        )

        if (state.loading) {
            LoadingFullScreen()
        }
    }
}

@Composable
private fun SignupScreenContent(
    state: SignupUiState,
    onNameUpdated: (String) -> Unit,
    onEmailUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onNameFocusChanged: (FocusState) -> Unit,
    onEmailFocusChanged: (FocusState) -> Unit,
    onTermsChecked: (Boolean) -> Unit,
    onTermsClicked: () -> Unit,
    onCreateClicked: () -> Unit,
    onDoneActionClicked: () -> Unit,
    onGoogleLoginClicked: () -> Unit,
    onFacebookLoginClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Toolbar(
            title = "Create an Account",
            showCloseIcon = true,
            onCloseClick = onBackClicked,
            isElevated = scrollState.canScrollBackward,
            modifier = Modifier
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .navigationBarsPadding()
                .padding(
                    vertical = 24.dp,
                    horizontal = 42.dp
                )
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameUpdated,
                label = { Text(text = stringResource(R.string.common_name)) },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .onFocusChanged(onNameFocusChanged)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameUpdated,
                label = { Text(text = stringResource(R.string.common_name)) },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .onFocusChanged(onNameFocusChanged)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameUpdated,
                label = { Text(text = stringResource(R.string.common_name)) },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .onFocusChanged(onNameFocusChanged)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailUpdated,
                label = { Text(text = stringResource(R.string.common_email)) },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .onFocusChanged(onEmailFocusChanged)
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            PasswordInputField(
                password = state.password,
                passwordStrengthItems = state.passwordStrengths,
                onPasswordUpdated = onPasswordUpdated,
                onDoneActionClicked = {
                    focusManager.clearFocus()
                    onDoneActionClicked()
                }
            )
            TermsAndConditionsCheckBox(
                isChecked = state.termsAccepted,
                onTermsChecked = onTermsChecked,
                onTermsClicked = onTermsClicked,
            )
            Button(
                enabled = state.createAccountButtonEnabled,
                onClick = onCreateClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            ) {
                Text(text = stringResource(id = R.string.signup_button_create))
            }
            FederatedIdentityButtons(
                onGoogleLoginClicked = onGoogleLoginClicked,
                onFacebookLoginClicked = onFacebookLoginClicked,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Composable
private fun PasswordInputField(
    password: String,
    passwordStrengthItems: List<PasswordStrengthItemState>,
    onPasswordUpdated: (String) -> Unit,
    onDoneActionClicked: () -> Unit,
) {
    Column {
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordUpdated,
            label = { Text(text = stringResource(R.string.common_password)) },
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onDoneActionClicked() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )
        passwordStrengthItems.forEach { strengthState ->
            PasswordStrengthItem(
                state = strengthState
            )
        }
    }
}

@Preview
@Composable
private fun SignupScreenPreview() {
    WaterAppTheme {
        SignupScreen(
            state = SignupUiState(
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
            ),
            onNameUpdated = {},
            onEmailUpdated = {},
            onPasswordUpdated = {},
            onNameFocusChanged = {},
            onEmailFocusChanged = {},
            onTermsChecked = {},
            onTermsClicked = {},
            onCreateClicked = {},
            onDoneActionClicked = {},
            onGoogleLoginClicked = {},
            onFacebookLoginClicked = {},
            onBackClicked = {},
        )
    }
}