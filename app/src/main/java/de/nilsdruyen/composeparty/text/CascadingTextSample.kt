package de.nilsdruyen.composeparty.text

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CascadingText() {
    val text = "Nils ist geil"
    var cascadingText = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        repeat(text.length) {
            delay(600)
            cascadingText.value = text.take(it + 1)
        }
//        delay(500)
//        cascadingText.value = "Nils"
//        delay(500)
//        cascadingText.value = "Nils ist"
//        delay(500)
//        cascadingText.value = "Nils ist geil"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        TextTransition(
            textAndColor = cascadingText.value to Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TextTransition(textAndColor: Pair<String, Color>, modifier: Modifier) {
    AnimatedContent(
        targetState = textAndColor,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "cascadingText",
        modifier = modifier,
    ) { (text, color) ->
        Row {
            text.forEachIndexed { index, char ->
                val stiffness = 300f - 200f * (index.toFloat() / (text.length - 1))
                Text(
                    modifier = Modifier.animateEnterExit(
                        enter = slideInVertically(
                            spring(
                                dampingRatio = 0.7f,
                                stiffness = stiffness
                            )
                        ) { -it },
                        exit = slideOutVertically { it }
                    ),
                    text = char.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = color
                )
            }
        }
    }
}

@Preview
@Composable
private fun CascadingTextPreview() {
    MaterialTheme {
        CascadingText()
    }
}