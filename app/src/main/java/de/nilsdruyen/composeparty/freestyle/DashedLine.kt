package de.nilsdruyen.composeparty.freestyle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DashedLine() {
    val px = remember { 10.dp }
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
        ) {
            val items = size.height / 2
            val dashHeight = 4.dp.toPx()
            var offset = 0f
            repeat(items.roundToInt()) {
                drawRect(
                    color = Color.Black,
                    topLeft = Offset(0f, offset),
                    size = Size(size.width, dashHeight)
                )
                offset += dashHeight * 2
            }
        }
    }
}