package com.leoapps.waterapp.auth.signup.presentation.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoapps.waterapp.R
import com.leoapps.waterapp.auth.signup.presentation.model.PasswordStrengthItemState
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme

@Composable
fun PasswordStrengthItem(
    state: PasswordStrengthItemState,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.Start),
        modifier = modifier.padding(vertical = 3.dp)
    ) {
        Image(
            painter = painterResource(id = state.checkResult.iconResId),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = stringResource(id = state.textResId),
            style = TextStyle.Default.copy(fontSize = 12.sp),
        )
    }
}

class PasswordStrengthStateProvider : PreviewParameterProvider<PasswordStrengthItemState> {
    override val values = listOf(
        PasswordStrengthItemState(
            textResId = R.string.password_validation_lowercase,
            checkResult = PasswordStrengthItemState.CheckResult.NOT_CHECKED
        ),
        PasswordStrengthItemState(
            textResId = R.string.password_validation_digits,
            checkResult = PasswordStrengthItemState.CheckResult.CHECK_SUCCED
        ),
    ).asSequence()
}

@Preview
@Composable
private fun PasswordStrengthItemPreview(
    @PreviewParameter(PasswordStrengthStateProvider::class) state: PasswordStrengthItemState
) {
    WaterAppTheme {
        PasswordStrengthItem(
            state = state
        )
    }
}