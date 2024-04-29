package com.leoapps.waterapp.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoapps.waterapp.ui.theme.NavyDark
import com.leoapps.waterapp.ui.theme.WaterAppTheme
import com.leoapps.waterapp.ui.theme.White

@Composable
fun ToggleNavBar(
    tabs: List<NavBarTab>,
    onTabClicked: (NavBarTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(64.dp)
            .fillMaxWidth()
            .background(
                color = NavyDark,
                shape = RoundedCornerShape(32.dp),
            )
            .padding(8.dp)
    ) {
        tabs.forEach { tab ->
            ToggleNavBarTab(
                title = tab.title,
                isSelected = tab.isSelected,
                onTabClicked = { onTabClicked(tab) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ToggleNavBarTab(
    title: String,
    isSelected: Boolean,
    onTabClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = remember(isSelected) { if (isSelected) White else NavyDark }
    val textColor = remember(isSelected) { if (isSelected) NavyDark else White }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxHeight()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(32.dp),
            )
            .clickable(onClick = onTabClicked)
    ) {
        Text(
            text = title,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun ToggleNavBarPreview() {
    WaterAppTheme {
        ToggleNavBar(
            tabs = listOf(
                NavBarTab(
                    id = NavBarTab.TabId.DAY,
                    title = "Day",
                    isSelected = true
                ),
                NavBarTab(
                    id = NavBarTab.TabId.WEEK,
                    title = "Week",
                    isSelected = false
                ),
            ),
            onTabClicked = { }
        )
    }
}

data class NavBarTab(
    val id: Any,
    val title: String,
    val isSelected: Boolean,
) {
    enum class TabId {
        DAY, WEEK
    }
}
