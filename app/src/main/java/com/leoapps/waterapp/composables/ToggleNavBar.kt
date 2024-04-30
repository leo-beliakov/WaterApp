package com.leoapps.waterapp.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.leoapps.waterapp.R
import com.leoapps.waterapp.ui.theme.NavyDark
import com.leoapps.waterapp.ui.theme.WaterAppTheme
import com.leoapps.waterapp.ui.theme.White

@Composable
fun ToggleNavBar(
    tabs: List<NavBarTab>,
    size: NavBarSize = NavBarSize.MEDIUM,
    colorScheme: ColorScheme = ColorScheme.PRIMARY,
    onTabClicked: (NavBarTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(size.sizeDp)
            .fillMaxWidth()
            .background(
                color = colorScheme.primaryColor,
                shape = RoundedCornerShape(32.dp),
            )
            .padding(8.dp)
    ) {
        tabs.forEach { tab ->
            ToggleNavBarTab(
                tab = tab,
                colorScheme = colorScheme,
                onTabClicked = { onTabClicked(tab) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ToggleNavBarTab(
    tab: NavBarTab,
    colorScheme: ColorScheme,
    onTabClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = remember(tab.isSelected) {
        if (tab.isSelected) colorScheme.accentColor else colorScheme.primaryColor
    }
    val contentColor = remember(tab.isSelected) {
        if (tab.isSelected) colorScheme.primaryColor else colorScheme.accentColor
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        modifier = modifier
            .fillMaxHeight()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(32.dp),
            )
            .clickable(onClick = onTabClicked)
    ) {
        tab.iconResId?.let { iconId ->
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(30.dp)
            )
        }
        tab.titleResId?.let { titleId ->
            Text(
                text = stringResource(id = titleId),
                color = contentColor,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun ToggleNavBarTextPreview() {
    WaterAppTheme {
        ToggleNavBar(
            size = NavBarSize.MEDIUM,
            colorScheme = ColorScheme.INVERSED,
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

@Preview
@Composable
private fun ToggleNavBarIconPreview() {
    WaterAppTheme {
        ToggleNavBar(
            size = NavBarSize.LARGE,
            colorScheme = ColorScheme.PRIMARY,
            tabs = listOf(
                NavBarTab(
                    id = HomeTimePeriodTabId.DAY,
                    iconResId = R.drawable.ic_drop,
                    isSelected = true
                ),
                NavBarTab(
                    id = HomeTimePeriodTabId.WEEK,
                    iconResId = R.drawable.ic_bottle,
                    isSelected = false
                ),
                NavBarTab(
                    id = HomeTimePeriodTabId.WEEK,
                    iconResId = R.drawable.ic_profile,
                    isSelected = false
                ),
            ),
            onTabClicked = { }
        )
    }
}

data class NavBarTab(
    val id: Any,
    @DrawableRes val iconResId: Int? = null,
    @StringRes val titleResId: Int? = null,
    val isSelected: Boolean,
)

enum class ColorScheme(
    val primaryColor: Color,
    val accentColor: Color,
) {
    PRIMARY(
        primaryColor = White,
        accentColor = NavyDark,
    ),
    INVERSED(
        primaryColor = NavyDark,
        accentColor = White,
    )
}

enum class NavBarSize(val sizeDp: Dp) {
    SMALL(56.dp),
    MEDIUM(64.dp),
    LARGE(88.dp)
}