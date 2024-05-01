package com.leoapps.waterapp.home.day.composables.progress

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class ProgressState(initial: Float) {

    var value: Float by mutableFloatStateOf(initial)
        private set

    suspend fun animateProgressTo(progress: Float) {
        animate(
            initialValue = value,
            targetValue = progress,
            initialVelocity = 0f,
            animationSpec = tween(
                durationMillis = 200,
                easing = LinearEasing
            ),
        ) { animatedValue, velocity ->
            value = animatedValue
        }
    }

    companion object {
        val Saver: Saver<ProgressState, *> = Saver(
            save = { it.value },
            restore = { ProgressState(it) }
        )
    }
}

@Composable
fun rememberProgressState(initial: Float = 0f): ProgressState {
    return rememberSaveable(saver = ProgressState.Saver) {
        ProgressState(initial = initial)
    }
}