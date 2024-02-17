package de.nilsdruyen.composeparty.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.material.LargeContent
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarSample(modifier: Modifier = Modifier) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var active by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf("") }

    BackHandler(active) {
        active = false
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .navigationBarsPadding(),
        topBar = {
            Column {
                LargeTopAppBar(
                    title = { Text("Hallo Large AppBar") },
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.PersonOutline,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                            )
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )

            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) {
        Box(Modifier.padding(it)) {
            LargeContent(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
            SearchBar(
                query = input,
                onQueryChange = { input = it },
                placeholder = {
                    Text("Suche")
                },
                onSearch = {
                    active = true
                },
                active = active,
                onActiveChange = {

                },
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                BackHandler(active) {
                    active = false
                }

                Text("Hallo")
            }
        }
    }
}

@Preview
@Composable
private fun AppBarSamplePreview() {
    ComposePartyTheme {
        AppBarSample(Modifier.fillMaxSize())
    }
}