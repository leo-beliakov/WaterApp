package com.leoapps.waterapp.auth.signup.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiEffect
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiState
import com.leoapps.waterapp.auth.signup.presentation.navigation.SignupNavigator
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import com.leoapps.waterapp.common.utils.clickableWithoutRipple

@Composable
fun SignupScreen(
    viewModel: SignupViewModel = hiltViewModel(),
    navigator: SignupNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignupScreen(
        state = state,
        onNameUpdated = viewModel::onNameUpdated,
        onEmailUpdated = viewModel::onEmailUpdated,
        onPasswordUpdated = viewModel::onPasswordUpdated,
        onTermsChecked = viewModel::onTermsChecked,
        onTermsClicked = viewModel::onTermsClicked,
        onCreateClicked = viewModel::onCreateClicked,
        onBackClicked = viewModel::onBackClicked,
        onDoneActionClicked = viewModel::onDoneActionClicked,
    )

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            SignupUiEffect.GoBack -> navigator.goBack()
        }
    }
}


@Composable
private fun SignupScreen(
    state: SignupUiState,
    onNameUpdated: (String) -> Unit,
    onEmailUpdated: (String) -> Unit,
    onPasswordUpdated: (String) -> Unit,
    onTermsChecked: (Boolean) -> Unit,
    onTermsClicked: () -> Unit,
    onBackClicked: () -> Unit,
    onCreateClicked: () -> Unit,
    onDoneActionClicked: () -> Unit,
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
        val focusManager = LocalFocusManager.current

        Text(
            text = "text",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        OutlinedTextField(
            value = state.name,
            onValueChange = onNameUpdated,
            label = { Text(text = "Full Name") },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Checkbox(
                checked = state.termsAccepted,
                onCheckedChange = onTermsChecked
            )
            Text(
                text = "I agree to all the ",
                style = TextStyle(
                    fontSize = 16.sp
                )
            )
            Text(
                text = "Terms & Conditions.",
                style = TextStyle(
                    fontSize = 16.sp
                ),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickableWithoutRipple(onClick = onTermsClicked)
            )
        }
        Button(
            onClick = onCreateClicked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create Account")
        }
    }
}