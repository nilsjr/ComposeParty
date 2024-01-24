package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDemo(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val state = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false,
        )
    )
    BottomSheetScaffold(
        scaffoldState = state,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Column(modifier) {
                Button(onClick = {
                    scope.launch {
                        state.bottomSheetState.hide()
                    }
                }) {
                    Text("click me")
                }
                repeat(5) {
                    Text("Hallo Nils $it")
                }
            }
        },
        topBar = {
            TopAppBar(title = { Text("BottomSheetTitle") })
        },
        modifier = modifier,
    ) {
        Column(Modifier.padding(it)) {
            Button(onClick = {
                scope.launch {
                    state.bottomSheetState.expand()
                }
            }) {
                Text("click me")
            }
            repeat(5) {
                Text("Hallo Nils $it")
            }
        }
    }
}

@Preview
@Composable
private fun BottomSheetDemoPreview() {
    ComposePartyTheme {
        BottomSheetDemo(Modifier.fillMaxSize())
    }
}