package de.nilsdruyen.composeparty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import de.nilsdruyen.composeparty.animations.TextAnim
import de.nilsdruyen.composeparty.buttons.AddToCartButtonDemo
import de.nilsdruyen.composeparty.buttons.ChipButton
import de.nilsdruyen.composeparty.buttons.LoadingButtonDemo
import de.nilsdruyen.composeparty.buttons.PreviewLoadingButton
import de.nilsdruyen.composeparty.freestyle.ComposeLogo
import de.nilsdruyen.composeparty.freestyle.DashedLine
import de.nilsdruyen.composeparty.freestyle.DynamicPointMesh
import de.nilsdruyen.composeparty.freestyle.HeartRate
import de.nilsdruyen.composeparty.freestyle.MenuToClose
import de.nilsdruyen.composeparty.freestyle.SquareFun
import de.nilsdruyen.composeparty.isles.IsleExample
import de.nilsdruyen.composeparty.material.ScrollableScaffold
import de.nilsdruyen.composeparty.paths.VectorDemo
import de.nilsdruyen.composeparty.text.WrappingText
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            ComposePartyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val screen = remember { mutableStateOf("") }

                    BackHandler(screen.value.isNotEmpty()) {
                        screen.value = ""
                    }

                    Crossfade(targetState = screen.value) {
                        if (it.isNotEmpty()) {
                            demoItems[it]?.invoke()
                        } else {
                            ItemList { path ->
                                screen.value = path
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemList(onItemClicked: (String) -> Unit) {
    Column(
        modifier = Modifier.systemBarsPadding()
    ) {
        demoItems.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        onItemClicked(it.key)
                    }
            ) {
                Text(
                    text = it.key,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 16.dp)
                )
            }
        }
    }
}

val demoItems = mapOf<String, @Composable () -> Unit>(
    "Wrapping Text" to { WrappingText() },
    "IsleExample" to { IsleExample() },
    "DynamicPointMesh" to { DynamicPointMesh() },
    "SquareFun" to { SquareFun() },
    "PreviewLoadingButton" to { PreviewLoadingButton() },
    "TextAnim" to { TextAnim() },
    "VectorDemo" to { VectorDemo() },
    "LoadingButtonDemo" to { LoadingButtonDemo() },
    "ChipButton" to { ChipButton() },
    "AddToCartButtonDemo" to { AddToCartButtonDemo() },
    "HeartRate" to { HeartRate() },
    "ComposeLogo" to { ComposeLogo() },
    "MenuToClose" to { MenuToClose() },
    "ScrollableScaffold" to { ScrollableScaffold() },
    "DashedLine" to { DashedLine() },
)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposePartyTheme {
        IsleExample()
    }
}