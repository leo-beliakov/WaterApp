package com.leoapps.waterapp.common.presentation.composables.progress_button

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.leoapps.waterapp.R
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme

@Composable
fun ProgressButton(
    state: ProgressButtonState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        enabled = state.isEnabled,
        onClick = { if (!state.isLoading) onClick() },
        modifier = modifier
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(30.dp),
                color = Color.White
            )
        } else {
            Text(text = stringResource(state.textResId))
        }
    }
}

private class ButtonStateProvider : PreviewParameterProvider<ProgressButtonState> {
    override val values = listOf(
        ProgressButtonState(
            isEnabled = true,
            isLoading = false,
            textResId = R.string.common_login
        ),
        ProgressButtonState(
            isEnabled = false,
            isLoading = false,
            textResId = R.string.common_login
        ),
        ProgressButtonState(
            isEnabled = true,
            isLoading = true,
            textResId = R.string.common_login
        ),
    ).asSequence()
}

@Preview
@Composable
private fun ProgressButtonPreview(
    @PreviewParameter(ButtonStateProvider::class) state: ProgressButtonState
) {
    WaterAppTheme {
        ProgressButton(
            state = state,
            onClick = {}
        )
    }
}
