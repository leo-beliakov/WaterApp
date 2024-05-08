package com.leoapps.waterapp.auth.login.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiEffect
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiState
import com.leoapps.waterapp.auth.login.presentation.navigation.LoginNavigator
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButton
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import com.leoapps.waterapp.common.utils.clickableWithoutRipple

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigator: LoginNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
    )

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            LoginUiEffect.CloseAuth -> navigator.closeAuth()
            LoginUiEffect.OpenSignUp -> navigator.openSignup()
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
    onFacebookLoginClicked: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
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
        MiddleScreenDivider(
            modifier = Modifier.padding(top = 24.dp)
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

@Composable
private fun FederatedIdentityButtons(
    onGoogleLoginClicked: () -> Unit,
    onFacebookLoginClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        OutlinedButton(
            onClick = onGoogleLoginClicked,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
        OutlinedButton(
            onClick = onFacebookLoginClicked,
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_facebook),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
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
    ProgressButton(
        state = state.buttonState,
        onClick = onLoginClicked,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red
        )
    )
}

@Composable
private fun MiddleScreenDivider(
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(color = Color.Gray)
        )
        Text(
            text = "or",
            color = Color.Gray
        )
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(2.dp)
                .background(color = Color.Gray)
        )
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
        )
    }
}