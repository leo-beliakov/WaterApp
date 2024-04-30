package com.leoapps.waterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.leoapps.waterapp.composables.HomeTimePeriodTabId
import com.leoapps.waterapp.composables.NavBarTab
import com.leoapps.waterapp.composables.ToggleNavBar
import com.leoapps.waterapp.ui.theme.WaterAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WaterAppTheme {
                // A surface container using the 'background' color from the theme
                ToggleNavBar(
                    tabs = listOf(
                        NavBarTab(
                            id = HomeTimePeriodTabId.DAY,
                            titleResId = R.string.home_navbar_tab_day,
                            isSelected = true
                        ),
                        NavBarTab(
                            id = HomeTimePeriodTabId.WEEK,
                            titleResId = R.string.home_navbar_tab_week,
                            isSelected = false
                        ),
                    ),
                    onTabClicked = { }
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WaterAppTheme {
        Greeting("Android")
    }
}