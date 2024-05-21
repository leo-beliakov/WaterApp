package com.leoapps.waterapp.water

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun WaterScreen(
    viewModel: WaterViewModel = hiltViewModel()
) {
    val waterData by viewModel.waterData.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    var newGoal by remember { mutableStateOf(waterData?.goal?.toString() ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Set Your Water Goal", style = TextStyle.Default)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newGoal,
            onValueChange = { newGoal = it },
            label = { Text("Water Goal (ml)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val goal = newGoal.toIntOrNull()
            if (goal != null) {
                viewModel.setWaterGoal(goal)
            }
        }) {
            Text("Save Goal")
        }

        if (error != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = error!!)
        }
    }
}
