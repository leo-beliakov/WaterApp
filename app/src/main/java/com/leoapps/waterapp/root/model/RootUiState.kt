package com.leoapps.waterapp.root.model

import com.leoapps.waterapp.composables.tab_bar.TabBarTab
import com.leoapps.waterapp.composables.tab_bar.TabId

data class RootUiState(
    val selectedTabId: TabId,
    val tabs: List<TabBarTab>
)