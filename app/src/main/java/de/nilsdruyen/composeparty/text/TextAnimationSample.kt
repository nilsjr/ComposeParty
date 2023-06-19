package de.nilsdruyen.composeparty.text

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.lerp
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TextAnimationSample() {
    var startStyle by remember { mutableStateOf(randomTextStyle()) }
    var endStyle by remember { mutableStateOf(randomTextStyle()) }
    var animate by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (animate) 0f else 1f,
        animationSpec = tween(durationMillis = 1_000),
        label = "textStyleFloat",
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(2_000)
            when (progress) {
                1f -> startStyle = randomTextStyle()
                0f -> endStyle = randomTextStyle()
            }
            animate = !animate
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Animated Text",
            modifier = Modifier.align(Alignment.Center),
            style = lerp(startStyle, endStyle, progress),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

fun randomTextStyle(): TextStyle = TextStyle(
    color = listOf(Color.Red, Color.Blue, Color.Black, Color.DarkGray, Color.Magenta).random(),
    fontSize = (20..60).random().sp,
    fontWeight = FontWeight((100..800).random()),
    fontStyle = listOf(FontStyle.Italic, FontStyle.Normal).random(),
    letterSpacing = (1..15).random().sp,
)