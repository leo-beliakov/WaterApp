package com.leoapps.waterapp.auth.signup.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leoapps.waterapp.R
import com.leoapps.waterapp.auth.signup.presentation.composables.PasswordStrengthItem
import com.leoapps.waterapp.auth.signup.presentation.model.PasswordStrengthItemState
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiEffect
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiState
import com.leoapps.waterapp.auth.signup.presentation.navigation.SignupNavigator
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButton
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButtonState
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
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
            SignupUiEffect.CloseAuth -> navigator.closeAuth()
            is SignupUiEffect.OpenUrl -> navigator.openUri(effect.url)
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
        PasswordInputField(
            password = state.password,
            passwordStrengthItems = state.passwordStrengths,
            onPasswordUpdated = onPasswordUpdated,
            onDoneActionClicked = {
                focusManager.clearFocus()
                onDoneActionClicked()
            }
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
        ProgressButton(
            state = state.buttonState,
            onClick = onCreateClicked,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun PasswordInputField(
    password: String,
    passwordStrengthItems: List<PasswordStrengthItemState>,
    onPasswordUpdated: (String) -> Unit,
    onDoneActionClicked: () -> Unit,
) {
    LazyColumn {
        item {
            OutlinedTextField(
                value = password,
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
                    .padding(vertical = 12.dp)
            )
        }
        items(
            items = passwordStrengthItems,
            key = { it.textResId }
        ) { strengthState ->
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
                buttonState = ProgressButtonState(
                    isEnabled = false,
                    isLoading = false,
                    textResId = R.string.sugnup_button_create,
                )
            ),
            onNameUpdated = {},
            onEmailUpdated = {},
            onPasswordUpdated = {},
            onTermsChecked = {},
            onTermsClicked = {},
            onBackClicked = {},
            onCreateClicked = {},
            onDoneActionClicked = {},
        )
    }
}