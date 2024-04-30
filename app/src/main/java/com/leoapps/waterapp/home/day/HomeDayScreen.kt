package com.leoapps.waterapp.home.day

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.leoapps.waterapp.home.day.composables.CircleProgress


@Composable
fun HomeDayScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircleProgress(
            title = "40%",
            subtitle = "1245 ml",
            progress = 0.4f,
            modifier = Modifier.size(300.dp),
        )
    }
}