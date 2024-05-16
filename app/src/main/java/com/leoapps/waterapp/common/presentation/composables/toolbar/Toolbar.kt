package com.leoapps.waterapp.common.presentation.composables.toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme
import com.leoapps.waterapp.common.presentation.utils.thenIf

private const val ICON_SIZE_DP = 40

@Composable
fun Toolbar(
    title: String,
    showCloseIcon: Boolean,
    onCloseClick: () -> Unit = {},
    isElevated: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .thenIf(isElevated) { this.shadow(8.dp) }
            .background(Color.White)
            .statusBarsPadding()
            .padding(
                horizontal = 12.dp,
                vertical = 16.dp
            )
            .heightIn(min = 48.dp)
    ) {
        if (showCloseIcon) {
            IconButton(
                onClick = onCloseClick,
                modifier = Modifier.size(ICON_SIZE_DP.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        }
        Text(
            text = title,
            style = TextStyle.Default.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f, true)
        )
        if (showCloseIcon) {
            Spacer(
                modifier = Modifier.size(ICON_SIZE_DP.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ToolbarPreview() {
    WaterAppTheme {
        Toolbar(
            title = "Create an Account",
            showCloseIcon = false
        )
    }
}

@Preview
@Composable
private fun ToolbarWithBackIconPreview() {
    WaterAppTheme {
        Toolbar(
            title = "Create an Account",
            showCloseIcon = true,
            onCloseClick = {}
        )
    }
}