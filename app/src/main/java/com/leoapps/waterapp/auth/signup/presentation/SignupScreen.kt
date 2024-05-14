package com.leoapps.waterapp.auth.signup.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
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
import com.leoapps.waterapp.auth.signup.presentation.model.SignUpUiEffect
import com.leoapps.waterapp.auth.signup.presentation.model.SignupUiState
import com.leoapps.waterapp.auth.signup.presentation.navigation.SignupNavigator
import com.leoapps.waterapp.common.presentation.composables.loading_fullscreen.LoadingFullScreen
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButton
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButtonState
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle

@Composable
fun SignupScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    navigator: SignupNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
    )

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            SignUpUiEffect.GoBack -> navigator.goBack()
            SignUpUiEffect.CloseAuth -> navigator.closeAuth()
            is SignUpUiEffect.OpenUrl -> navigator.openUri(effect.url)
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
        )

        if (state.loading) {
            LoadingFullScreen()
        }
    }
}

@Composable
fun SignupScreenContent(
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
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
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
        ProgressButton(
            state = state.buttonState,
            onClick = onCreateClicked,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            )
        )
    }
}

@Composable
fun TermsAndConditionsCheckBox(
    isChecked: Boolean,
    onTermsChecked: (Boolean) -> Unit,
    onTermsClicked: () -> Unit

) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = 24.dp)
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onTermsChecked
        )
        TermsAndConditionsText(
            onTermsClicked = onTermsClicked
        )
    }
}

@Composable
fun TermsAndConditionsText(onTermsClicked: () -> Unit) {
    val resources = LocalContext.current.resources
    val fullText = resources.getString(R.string.signup_terms_and_conditions_full)
    val termsText = resources.getString(R.string.signup_terms_and_conditions)

    val annotatedString = buildAnnotatedString {
        append(fullText.substringBefore(termsText))
        pushStringAnnotation(tag = "URL", annotation = "open_terms")
        pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
        append(termsText)
        pop()  // Pop style for underlining
        pop()  // Pop annotation
        append(fullText.substringAfter(termsText))
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(fontSize = 16.sp),
        onClick = { offset ->
            val clickedUrl = annotatedString
                .getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()

            if (clickedUrl?.item == "open_terms") {
                onTermsClicked.invoke()
            }
        }
    )
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
                buttonState = ProgressButtonState(
                    isEnabled = false,
                    isLoading = false,
                    textResId = R.string.signup_button_create,
                )
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
        )
    }
}