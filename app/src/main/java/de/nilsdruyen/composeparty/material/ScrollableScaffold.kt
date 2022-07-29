package de.nilsdruyen.composeparty.material

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScrollableScaffold() {
    val barState = rememberTopAppBarState()
    val scrollBehavior = remember {
        TopAppBarDefaults.enterAlwaysScrollBehavior(barState)
    }

    val systemUiController = rememberSystemUiController()
    val isInDarkTheme = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = !isInDarkTheme
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(text = "Hallo")
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) {
        LargeContent(
            Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(rememberScrollState())
        )
    }
}


@Composable
fun LargeContent(modifier: Modifier) {
    Column(modifier = modifier) {
        repeat(30) {
            Content(input = it.toString())
        }
    }
}

@Composable
fun Content(input: String) {
    Text(text = "test $input", modifier = Modifier
        .clickable { }
        .padding(16.dp))
}