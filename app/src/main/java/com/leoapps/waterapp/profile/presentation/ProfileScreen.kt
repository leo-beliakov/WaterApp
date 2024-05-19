package com.leoapps.waterapp.water

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.presentation.composables.progress_button.ProgressButton
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
import com.leoapps.waterapp.common.utils.CollectEventsWithLifecycle
import com.leoapps.waterapp.profile.presentation.ProfileViewModel
import com.leoapps.waterapp.profile.presentation.model.ProfileUiEffect
import com.leoapps.waterapp.profile.presentation.model.ProfileUiState
import com.leoapps.waterapp.profile.presentation.navigation.ProfileNavigator

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigator: ProfileNavigator
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProfileScreen(
        state = state,
        onLoginClicked = viewModel::onLoginClicked,
        onLogOutClicked = viewModel::onLogOutClicked,
        onDeleteAccountClicked = viewModel::onDeleteAccountClicked,
    )

    CollectEventsWithLifecycle(viewModel.sideEffects) { effect ->
        when (effect) {
            ProfileUiEffect.OpenLogin -> navigator.openAuthScreen()
        }
    }
}

@Composable
private fun ProfileScreen(
    state: ProfileUiState,
    onLoginClicked: () -> Unit,
    onLogOutClicked: () -> Unit,
    onDeleteAccountClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(24.dp)
    ) {
        if (state.profileInfoVisible) {
            Text(
                text = state.username
            )
            Text(
                text = state.email
            )
            Text(
                text = state.goal
            )
            Text(
                text = state.progress
            )
            Text(
                text = state.previousRecords
            )
        }
        Spacer(
            modifier = Modifier.weight(
                weight = 1f,
                fill = true
            )
        )
        if (state.loginButtonVisible) {
            Button(
                onClick = onLoginClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.common_login)
                )
            }
        } else {
            Button(
                onClick = onLogOutClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Yellow
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.common_logout)
                )
            }
            ProgressButton(
                state = state.deleteButtonState,
                onClick = onDeleteAccountClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    WaterAppTheme {
        ProfileScreen(
            state = ProfileUiState(
                username = "Agent Smith",
                profileInfoVisible = false,
                loginButtonVisible = false,
            ),
            onLoginClicked = { },
            onLogOutClicked = { },
            onDeleteAccountClicked = { }
        )
    }
}