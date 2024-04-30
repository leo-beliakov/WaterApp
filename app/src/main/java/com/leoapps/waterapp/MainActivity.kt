package com.leoapps.waterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.leoapps.waterapp.root.RootScreen
import com.leoapps.waterapp.ui.theme.WaterAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaterAppTheme {
                // A surface container using the 'background' color from the theme
                RootScreen()
            }
        }
    }
}
