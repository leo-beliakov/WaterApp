package com.leoapps.waterapp.auth.login.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiEffect
import com.leoapps.waterapp.auth.login.presentation.model.LoginUiState
import com.leoapps.waterapp.auth.login.presentation.navigation.LoginNavigator
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    navigator: LoginNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        onEmailUpdated = viewModel::onEmailUpdated,
        onPasswordUpdated = viewModel::onPasswordUpdated,
        onDoneActionClicked = viewModel::onDoneActionClicked,
        onLoginClick = viewModel::onLoginButtonClicked,
        onSignupClicked = viewModel::onSignupClicked
    )

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            LoginUiEffect.GoBack -> navigator.openGoBack()
            LoginUiEffect.OpenSignUp -> navigator.openSignup()
        }
    }
}

@Composable
private fun LoginScreen(
    state: LoginUiState,
    onEmailUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onDoneActionClicked: () -> Unit,
    onLoginClick: () -> Unit,
    onSignupClicked: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                vertical = 24.dp,
                horizontal = 42.dp
            )
    ) {
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
            modifier = Modifier.fillMaxWidth()
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
                onDone = { onDoneActionClicked() }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        )
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(text = "Login")
        }
        Row(
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = "Don't have an account? "
            )
            Text(
                text = "Sign up",
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(onClick = onSignupClicked)
            )
        }
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
            onPasswordUpdated = {},
            onDoneActionClicked = {},
            onLoginClick = {},
            onSignupClicked = {}
        )
    }
}