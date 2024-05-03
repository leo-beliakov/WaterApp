package com.leoapps.waterapp.onboarding.presentation.composables

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoapps.waterapp.common.presentation.theme.PurpleMain
import com.leoapps.waterapp.common.presentation.theme.PurpleOpaque
import com.leoapps.waterapp.common.presentation.theme.WaterAppTheme

private const val BUTTON_DEFAULT_SIZE_DP = 70
private const val PULSATION_ANIMATION_DURATION = 1100

@Composable
fun PulsatingButton(
    icon: ImageVector = Icons.Default.ArrowForward,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        PulsatingCircle(
            modifier = Modifier.size(BUTTON_DEFAULT_SIZE_DP.dp)
        )
        CircleButton(
            icon = icon,
            contentDescription = contentDescription,
            onClick = onClick,
            modifier = Modifier.size(BUTTON_DEFAULT_SIZE_DP.dp)

        )
    }
}

@Composable
private fun CircleButton(
    icon: ImageVector = Icons.Default.ArrowForward,
    contentDescription: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(color = PurpleMain)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .size(22.dp)
        )
    }
}

@Composable
fun PulsatingCircle(modifier: Modifier) {
    val animationProgress = remember { Animatable(0f) }
    val animatedScale by remember { derivedStateOf { animationProgress.value * 0.9f + 1 } }
    val animatedAlpha by remember { derivedStateOf { 1f - animationProgress.value } }

    LaunchedEffect(true) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = InfiniteRepeatableSpec(
                animation = tween(
                    durationMillis = PULSATION_ANIMATION_DURATION,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = animatedScale
                scaleY = animatedScale
                alpha = animatedAlpha
            }
            .background(
                color = PurpleOpaque,
                shape = CircleShape
            )
    )
}

@Preview
@Composable
private fun PulsatingButtonPreview() {
    WaterAppTheme {
        PulsatingButton(
            onClick = { }
        )
    }
}