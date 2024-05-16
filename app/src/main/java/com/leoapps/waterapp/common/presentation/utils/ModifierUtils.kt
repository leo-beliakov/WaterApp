package com.leoapps.waterapp.common.presentation.utils

import androidx.compose.ui.Modifier

/**
 * Conditionally applies a modifier to the current Modifier.
 *
 * @param condition A Boolean that determines if the modifierBlock should be applied.
 * @param modifierBlock A lambda that takes the current Modifier and returns a modified Modifier.
 * @return The original or modified Modifier based on the condition.
 */
fun Modifier.thenIf(
    condition: Boolean,
    modifierBlock: Modifier.() -> Modifier
): Modifier {
    return if (condition) {
        this.modifierBlock()
    } else {
        this
    }
}