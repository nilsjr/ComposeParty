package de.nilsdruyen.composeparty.layouts

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import de.nilsdruyen.composeparty.R
import de.nilsdruyen.composeparty.material.LargeContent
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarSample(modifier: Modifier = Modifier) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    var active by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf("") }

    var selected by remember { mutableStateOf(false) }

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
        bottomBar = {
            Box {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(40.dp)
//                        .background(Color.Red)
//                )
                NavigationBar(
                    containerColor = Color.Transparent
                ) {
                    NavigationBarItem(
                        selected = selected,
                        onClick = { selected = !selected },
                        icon = { AnimatedIcon(selected = selected) },
                        label = { Text("Test") },
                        alwaysShowLabel = true,
                    )
                    NavigationBarItem(
                        selected = !selected,
                        onClick = { selected = !selected },
                        icon = { AnimatedIcon(selected = !selected) },
                        label = { Text("Test 2") },
                        alwaysShowLabel = true,
                    )
                }
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
                inputField = {
                    SearchBarDefaults.InputField(
                        query = input,
                        onQueryChange = { input = it },
                        onSearch = {
                            active = true
                        },
                        expanded = true,
                        onExpandedChange = {

                        },
                        placeholder = {
                            Text("Suche")
                        },
                    )
                },
                expanded = true,
                onExpandedChange = {

                },
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                BackHandler(active) {
                    active = false
                }

                Text("Hallo")
            }
            Box {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_search))
                val progress by animateLottieCompositionAsState(
                    composition,
                    iterations = LottieConstants.IterateForever,
                    speed = 1f,
                )
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                )
            }
        }
    }
}

@Composable
private fun AnimatedIcon(selected: Boolean, modifier: Modifier = Modifier) {
    Box {
        var isPlaying by remember { mutableStateOf(false) }
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation_search))
        val progressLottie by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isPlaying,
            restartOnPlay = false,
            iterations = 1,
            speed = if (selected) 1f else -1f,
        )

        LaunchedEffect(selected) {
            if (selected) {
                if (progressLottie == 0f) {
                    isPlaying = true
                }
                if (progressLottie == 1f) {
                    isPlaying = false
                }
            } else {
                if (progressLottie == 1f) {
                    isPlaying = true
                }
                if (progressLottie == 0f) {
                    isPlaying = false
                }
            }
        }

        LottieAnimation(
            composition = composition,
            progress = { progressLottie },
            modifier = modifier,
        )
    }
}

@Preview
@Composable
private fun AppBarSamplePreview() {
    ComposePartyTheme {
        AppBarSample(Modifier.fillMaxSize())
    }
}

@Preview
@Composable
private fun AnimatedIconPreview() {
    var selected by remember { mutableStateOf(false) }
    ComposePartyTheme {
        AnimatedIcon(selected, Modifier.clickable { selected = !selected })
    }
}