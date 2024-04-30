package com.leoapps.waterapp.composables.tab_bar

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.leoapps.waterapp.R
import com.leoapps.waterapp.home.HomeTimePeriodTabId
import com.leoapps.waterapp.root.model.RootTab
import com.leoapps.waterapp.ui.theme.WaterAppTheme

@Composable
fun TabBar(
    selectedTabId: TabId,
    tabs: List<TabBarTab>,
    size: TabBarSize = TabBarSize.MEDIUM,
    colorScheme: TabBarColorScheme = TabBarColorScheme.PRIMARY,
    onTabClicked: (TabBarTab) -> Unit,
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
            Tab(
                tab = tab,
                isSelected = selectedTabId == tab.id,
                colorScheme = colorScheme,
                onTabClicked = { onTabClicked(tab) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Tab(
    isSelected: Boolean,
    tab: TabBarTab,
    colorScheme: TabBarColorScheme,
    onTabClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = remember(isSelected) {
        if (isSelected) colorScheme.accentColor else colorScheme.primaryColor
    }
    val contentColor = remember(isSelected) {
        if (isSelected) colorScheme.primaryColor else colorScheme.accentColor
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(32.dp))
            .background(color = backgroundColor)
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
private fun TabBarTextPreview() {
    WaterAppTheme {
        TabBar(
            size = TabBarSize.MEDIUM,
            colorScheme = TabBarColorScheme.INVERSED,
            selectedTabId = HomeTimePeriodTabId.DAY.route,
            tabs = listOf(
                TabBarTab(
                    id = HomeTimePeriodTabId.DAY.route,
                    titleResId = R.string.home_navbar_tab_day,
                ),
                TabBarTab(
                    id = HomeTimePeriodTabId.WEEK.route,
                    titleResId = R.string.home_navbar_tab_week,
                ),
            ),
            onTabClicked = { }
        )
    }
}

@Preview
@Composable
private fun TabBarIconPreview() {
    WaterAppTheme {
        TabBar(
            size = TabBarSize.LARGE,
            colorScheme = TabBarColorScheme.PRIMARY,
            selectedTabId = RootTab.HOME.route,
            tabs = listOf(
                TabBarTab(
                    id = RootTab.HOME.route,
                    iconResId = R.drawable.ic_drop,
                ),
                TabBarTab(
                    id = RootTab.BOTTLE.route,
                    iconResId = R.drawable.ic_bottle,
                ),
                TabBarTab(
                    id = RootTab.PROFILE.route,
                    iconResId = R.drawable.ic_profile,
                ),
            ),
            onTabClicked = { }
        )
    }
}