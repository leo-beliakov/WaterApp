package com.leoapps.waterapp.common.presentation.composables.loading_fullscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leoapps.waterapp.common.presentation.theme.Black_Opaque_70
import com.leoapps.waterapp.common.utils.clickableWithoutRipple

@Composable
fun LoadingFullScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Black_Opaque_70)
            .clickableWithoutRipple { }
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.Center),
        )
    }
}