package de.nilsdruyen.composeparty.layouts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import de.nilsdruyen.composeparty.material.LargeContent
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipLayoutSample(modifier: Modifier = Modifier) {
//    val tooltipState = rememberTooltipState(isPersistent = true)
//    val scope = rememberCoroutineScope()

    WhatsNewOverlay {
        Scaffold(
            modifier = it,
            topBar = {
                TopAppBar(
                    title = { Text("Tooltip") },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.Default.Settings, contentDescription = null)
                        }
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Search, contentDescription = null)
                }
            }
        ) {
            LargeContent(modifier = Modifier.padding(it))
        }
//        TooltipBox(
//            positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
//            tooltip = {
//                RichTooltip(
//                    modifier = Modifier.fillMaxSize(),
//                    title = { Text("test") },
//                    action = {
//                        TextButton(
//                            onClick = { scope.launch { tooltipState.dismiss() } }
//                        ) { Text("action") }
//                    },
//                    caretProperties = CaretProperties(16.dp, 32.dp)
//                ) {
//                    Text("tooltipText")
//                }
//            },
//            state = tooltipState,
//            modifier = Modifier.padding(16.dp)
//        ) {
//            IconButton(
//                onClick = { /* Icon button's click event */ }
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Info,
//                    contentDescription = "Localized Description"
//                )
//            }
//        }
    }
}

@Composable
fun WhatsNewOverlay(modifier: Modifier = Modifier, content: @Composable (Modifier) -> Unit) {
    val hazeState = remember { HazeState() }

    Box(
        modifier = modifier
    ) {
        content(Modifier.haze(state = hazeState))
        Canvas(
            Modifier
                .fillMaxSize()
                .hazeChild(state = hazeState, style = HazeStyle(tint = Color.Black))
        ) {
            drawRect(Color.Black.copy(alpha = .3f))
        }
    }
}

@Preview
@Composable
private fun TooltipPreview() {
    ComposePartyTheme {
        TooltipLayoutSample(Modifier.fillMaxSize())
    }
}