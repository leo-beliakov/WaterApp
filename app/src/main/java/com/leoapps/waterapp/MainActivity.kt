package com.leoapps.waterapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.leoapps.waterapp.root.RootScreen
import com.leoapps.waterapp.ui.theme.WaterAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaterAppTheme {
                RootScreen()
            }
        }
    }
}

//TODO DONE:
//1.1. Make beverages buttons work - DONE
//2. Make progress change
//3. Make progress change animated
//4. Make progress animated when the screen first shown
//5. Make vibrations on buttons clicks
//6. Create a button for welcome screen
//7. Create a pulsation animation for that button
//8. Make current state persistent over sessions
//10. Make the size of the circle progress composable adaptive
//11. Refactor persistance code (Repo, etc) + clean up the VM

//TODO:
//9. Think about corner cases like negative balance or balance > 100%
//11. Predefined TextStyles