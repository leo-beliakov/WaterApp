package com.leoapps.waterapp.auth.login.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leoapps.waterapp.R
import com.leoapps.waterapp.auth.common.presentation.composables.FederatedIdentityButtons
import com.leoapps.waterapp.auth.common.presentation.rememberFacebookAuthHelper
import com.leoapps.waterapp.auth.common.presentation.rememberGoogleAuthHelper
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiEffect
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiState
import com.leoapps.waterapp.auth.login.presentation.navigation.LoginNavigator
import com.leoapps.waterapp.common.presentation.composables.loading_fullscreen.LoadingFullScreen
import com.leoapps.waterapp.common.presentation.composables.toolbar.Toolbar
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import com.leoapps.waterapp.common.utils.clickableWithoutRipple
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigator: LoginNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val googleAuthHelper = rememberGoogleAuthHelper()
    val facebookAuthHelper = rememberFacebookAuthHelper()

    LoginScreen(
        state = state,
        onEmailUpdated = viewModel::onEmailUpdated,
        onEmailFocusChanged = viewModel::onEmailFocusChanged,
        onPasswordUpdated = viewModel::onPasswordUpdated,
        onDoneActionClicked = viewModel::onDoneActionClicked,
        onLoginClicked = viewModel::onLoginButtonClicked,
        onSignupClicked = viewModel::onSignupClicked,
        onGoogleLoginClicked = viewModel::onGoogleLoginClicked,
        onFacebookLoginClicked = viewModel::onFacebookLoginClicked,
        onBackClicked = viewModel::onBackClicked,
    )

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            LoginUiEffect.NavigateClose -> navigator.closeAuth()
            LoginUiEffect.NavigateToSignUp -> navigator.openSignup()
            LoginUiEffect.ShowAuthFailed -> {
                Log.d("MyTag", "Failure to sign in")
            }

            LoginUiEffect.RequestFacebookAuth -> {
                facebookAuthHelper.auth(
                    onSuccess = viewModel::onFacebookAuthSuccess,
                    onFailure = viewModel::onFailedSignInResponse,
                )
            }

            is LoginUiEffect.RequestGoogleAuth -> {
                //For some reason, without switching to this scope we are getting
                //"Sign-in request cancelled by WaterApp" toast on UI
                scope.launch {
                    googleAuthHelper.auth(
                        request = effect.request,
                        onSuccess = viewModel::onGoogleAuthSuccess,
                        onFailure = viewModel::onFailedSignInResponse,
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginScreen(
    state: LoginUiState,
    onEmailUpdated: (String) -> Unit,
    onEmailFocusChanged: (FocusState) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onDoneActionClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    onSignupClicked: () -> Unit,
    onGoogleLoginClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onFacebookLoginClicked: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        LoginScreenContent(
            state = state,
            onEmailUpdated = onEmailUpdated,
            onEmailFocusChanged = onEmailFocusChanged,
            onPasswordUpdated = onPasswordUpdated,
            onDoneActionClicked = onDoneActionClicked,
            onLoginClicked = onLoginClicked,
            onSignupClicked = onSignupClicked,
            onGoogleLoginClicked = onGoogleLoginClicked,
            onBackClicked = onBackClicked,
            onFacebookLoginClicked = onFacebookLoginClicked,
        )

        if (state.loading) {
            LoadingFullScreen()
        }
    }
}

@Composable
private fun LoginScreenContent(
    state: LoginUiState,
    onEmailUpdated: (String) -> Unit,
    onEmailFocusChanged: (FocusState) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onDoneActionClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    onSignupClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onGoogleLoginClicked: () -> Unit,
    onFacebookLoginClicked: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Toolbar(
            title = stringResource(id = R.string.common_login),
            showCloseIcon = true,
            isElevated = scrollState.canScrollBackward,
            onCloseClick = onBackClicked,
        )
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .safeDrawingPadding()
                .padding(
                    vertical = 24.dp,
                    horizontal = 42.dp
                )
        ) {
            EmailLoginForm(
                state = state,
                onEmailUpdated = onEmailUpdated,
                onEmailFocusChanged = onEmailFocusChanged,
                onPasswordUpdated = onPasswordUpdated,
                onDoneActionClicked = onDoneActionClicked,
                onLoginClicked = onLoginClicked,
            )
            FederatedIdentityButtons(
                onGoogleLoginClicked = onGoogleLoginClicked,
                onFacebookLoginClicked = onFacebookLoginClicked,
                modifier = Modifier.padding(top = 24.dp)
            )
            SignupFooter(
                onSignupClicked = onSignupClicked,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun EmailLoginForm(
    state: LoginUiState,
    onEmailUpdated: (String) -> Unit,
    onEmailFocusChanged: (FocusState) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onDoneActionClicked: () -> Unit,
    onLoginClicked: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = state.email,
        onValueChange = onEmailUpdated,
        label = { Text(text = "Email") },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        isError = state.showEmailInvalidError,
        supportingText = if (state.showEmailInvalidError) {
            { Text(text = stringResource(id = R.string.common_invalid_email)) }
        } else {
            null
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged(onEmailFocusChanged)
    )
    OutlinedTextField(
        value = state.password,
        onValueChange = onPasswordUpdated,
        label = { Text(text = "Password") },
        maxLines = 1,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                onDoneActionClicked()
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    )
    Button(
        enabled = state.loginButtonEnabled,
        onClick = onLoginClicked,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
    ) {
        Text(text = stringResource(id = R.string.common_login))
    }
}

@Composable
private fun SignupFooter(
    onSignupClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = "Don't have an account? "
        )
        Text(
            text = "Sign up",
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickableWithoutRipple(onClick = onSignupClicked)
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    WaterAppTheme {
        LoginScreen(
            state = LoginUiState(
                email = "",
                password = ""
            ),
            onEmailUpdated = {},
            onEmailFocusChanged = {},
            onPasswordUpdated = {},
            onDoneActionClicked = {},
            onLoginClicked = {},
            onSignupClicked = {},
            onGoogleLoginClicked = {},
            onFacebookLoginClicked = {},
            onBackClicked = {},
        )
    }
}