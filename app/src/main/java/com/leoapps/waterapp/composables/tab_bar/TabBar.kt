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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leoapps.waterapp.home.root.model.HomeTab
import com.leoapps.waterapp.root.model.RootTab
import com.leoapps.waterapp.ui.theme.WaterAppTheme

@Composable
fun <T : TabBarTab> TabBar(
    selectedTab: T,
    tabs: List<T>,
    size: TabBarSize = TabBarSize.MEDIUM,
    colorScheme: TabBarColorScheme = TabBarColorScheme.PRIMARY,
    onTabClicked: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(size.sizeDp)
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(32.dp),
            )
            .background(
                color = colorScheme.primaryColor,
                shape = RoundedCornerShape(32.dp),
            )
            .padding(8.dp)
    ) {
        tabs.forEach { tab ->
            Tab(
                tab = tab,
                isSelected = selectedTab == tab,
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
                style = TextStyle.Default.copy(
                    color = contentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700)
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun TabBarTextPreview() {
    WaterAppTheme {
        TabBar<HomeTab>(
            size = TabBarSize.MEDIUM,
            colorScheme = TabBarColorScheme.INVERTED,
            selectedTab = HomeTab.Day,
            tabs = listOf(
                HomeTab.Day,
                HomeTab.Week
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
            selectedTab = RootTab.Home,
            tabs = listOf(
                RootTab.Home,
                RootTab.Bottle,
                RootTab.Profile
            ),
            onTabClicked = { }
        )
    }
}