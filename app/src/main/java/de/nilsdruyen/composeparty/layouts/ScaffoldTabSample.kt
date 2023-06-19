package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldTabSample() {
    // Tabs for pager
    val tabData = listOf(
        "Tab 1",
        "Tab 2",
    )

    // Pager state
    val pagerState = rememberPagerState { 2 }
    // Coroutine scope for scroll pager
    val coroutineScope = rememberCoroutineScope()
    // Scroll behavior for TopAppBar
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = "Top app bar")
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
            )
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabData.forEachIndexed { index, s ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    ) {
                        Text(s, Modifier.padding(8.dp))
                    }
                }
            }
            HorizontalPager(state = pagerState) {
                when (it) {
                    0 -> ContentList()
                    1 -> ContentList()
                }
            }
        }
    }
}

@Composable
fun ContentList() {
    LazyColumn(Modifier.fillMaxSize()) {
        items(List(20) { "Test $it" }) {
            ListItem(headlineContent = { Text(text = it, Modifier.padding(8.dp)) })
        }
    }
}