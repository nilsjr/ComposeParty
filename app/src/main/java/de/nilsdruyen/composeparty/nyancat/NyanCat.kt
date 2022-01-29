package de.nilsdruyen.composeparty.nyancat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.Beige
import de.nilsdruyen.composeparty.ui.theme.Black
import de.nilsdruyen.composeparty.ui.theme.Grey
import de.nilsdruyen.composeparty.ui.theme.Pink
import de.nilsdruyen.composeparty.ui.theme.Purple
import de.nilsdruyen.composeparty.ui.theme.Rosa
import kotlinx.coroutines.delay

// block
// h23 x w34
// 10x10 block
// body
// 18 x 21
// h180 x w210

@Composable
fun NyanCatParty() {
    val frames = TailFrames
    val currentIndex = remember { mutableStateOf(0) }
    val order = remember { mutableStateOf(false) }

    val colors = listOf(Color.Red, Color.Green, Color.Yellow, Color.Cyan)

    LaunchedEffect(Unit) {
        while (true) {
            delay(130)
            val index = currentIndex.value
            currentIndex.value = if (order.value) {
                if (index == frames.indices.last) {
                    order.value = false
                    frames.indices.last - 1
                } else {
                    index + 1
                }
            } else {
                if (index == frames.indices.first) {
                    order.value = true
                    frames.indices.first + 1
                } else {
                    index - 1
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .size(600.dp, 400.dp)
            .background(Color(0xFFFFFFFF))
    ) {
        Canvas(
            modifier = Modifier
                .scale(2f)
                .background(Color.LightGray)
                .size(340.dp, 230.dp)
                .align(Alignment.Center)
        ) {
            rainbow(colors, center.minus(Offset(280f, 130f)))
            body(center.minus(Offset(100f, 100f)))
            tail(frames[currentIndex.value], center.minus(Offset(200f, 40f)))
            head(center.minus(Offset(20f, 70f)))
        }
    }
}

//private lateinit var customTypeface: android.graphics.Typeface
//customTypeface = resources.getFont(R.font.c64_pro_mono_style)

//@Composable
//fun C64TextDemo() {
//    Canvas(modifier = Modifier.fillMaxWidth().preferredHeight(128.dp),
//        onDraw = {
//            drawRect(Color(0xff525ce6))
//            val paint = Paint()
//            paint.textAlign = Paint.Align.CENTER
//            paint.textSize = 64f
//            paint.color = 0xffb0b3ff.toInt()
//            paint.typeface = customTypeface
//            drawContext.canvas.nativeCanvas.drawText(
//                "HELLO WORLD!",
//                center.x, center.y, paint
//            )
//        })
//}

@Preview(backgroundColor = 0xFFFFFF)
@Composable
fun NyanCatBlueprint() {
    Box {
        Canvas(
            modifier = Modifier
                .background(Color.White)
                .size(340.dp, 230.dp)
                .align(Alignment.Center)
        ) {
            draw(Body, BodyPosition)
            draw(Head, HeadPosition)
            TailFrames.forEachIndexed { index, item ->
                val offset = TailPosition.plus(Offset(0f, 100f * index))
                drawRect(Color.LightGray, offset, Size(9f, 9f).times(10f))
                draw(item, offset)
            }
            draw(RainBow, RainBowPosition)
        }
    }
}

fun DrawScope.body(offset: Offset) {
    draw(Body, offset.minus(Offset(9f, 9f)))
}

fun DrawScope.tail(list: List<Pixel>, offset: Offset) {
    draw(list, offset)
}

fun DrawScope.head(offset: Offset) {
    draw(Head, offset)
}

fun DrawScope.rainbow(colors: List<Color>, offset: Offset) {
    val heights = listOf(0f, 2f, 1f, 3f)
    val parts = 4
    colors.forEachIndexed { index, color ->
        repeat(parts) { part ->
            drawRect(
                color,
                topLeft = Offset(6f * part, heights[part] + 4f * index).times(PxSize).plus(offset),
                size = Size(6f, 4f).times(PxSize)
            )
        }
    }
}

fun DrawScope.draw(list: List<Pixel>, offset: Offset) {
    list.forEach {
        drawRect(
            it.color,
            topLeft = it.offset.times(PxSize).plus(offset),
            size = it.size.times(PxSize)
        )
    }
}

const val PxSize = 10f
const val PxWidth = 20f
const val PxHeight = 20f

data class Pixel(val offset: Offset, val size: Size, val color: Color)

val HeadPosition = Offset(40f, 250f)
val Head = listOf(
    // border
    Pixel(Offset(2f, 0f), Size(2f, 1f), Black),
    Pixel(Offset(4f, 1f), Size(1f, 1f), Black),
    Pixel(Offset(5f, 2f), Size(1f, 1f), Black),
    Pixel(Offset(6f, 3f), Size(4f, 1f), Black),
    Pixel(Offset(10f, 2f), Size(1f, 1f), Black),
    Pixel(Offset(11f, 1f), Size(1f, 1f), Black),
    Pixel(Offset(12f, 0f), Size(2f, 1f), Black),
    Pixel(Offset(14f, 1f), Size(1f, 4f), Black),
    Pixel(Offset(15f, 5f), Size(1f, 5f), Black),
    Pixel(Offset(14f, 10f), Size(1f, 1f), Black),
    Pixel(Offset(13f, 11f), Size(1f, 1f), Black),
    Pixel(Offset(3f, 12f), Size(10f, 1f), Black),
    Pixel(Offset(2f, 11f), Size(1f, 1f), Black),
    Pixel(Offset(1f, 10f), Size(1f, 1f), Black),
    Pixel(Offset(0f, 5f), Size(1f, 5f), Black),
    Pixel(Offset(1f, 1f), Size(1f, 4f), Black),
    // bg
    Pixel(Offset(2f, 1f), Size(2f, 4f), Grey),
    Pixel(Offset(4f, 2f), Size(1f, 3f), Grey),
    Pixel(Offset(5f, 3f), Size(1f, 1f), Grey),
    Pixel(Offset(5f, 4f), Size(9f, 1f), Grey),
    Pixel(Offset(10f, 3f), Size(4f, 1f), Grey),
    Pixel(Offset(11f, 2f), Size(3f, 1f), Grey),
    Pixel(Offset(12f, 1f), Size(2f, 1f), Grey),
    Pixel(Offset(1f, 5f), Size(14f, 5f), Grey),
    Pixel(Offset(2f, 10f), Size(12f, 1f), Grey),
    Pixel(Offset(3f, 11f), Size(10f, 1f), Grey),
    // cheek
    Pixel(Offset(2f, 8f), Size(2f, 2f), Rosa),
    Pixel(Offset(13f, 8f), Size(2f, 2f), Rosa),
    // mouth
    Pixel(Offset(5f, 10f), Size(7f, 1f), Black),
    Pixel(Offset(5f, 9f), Size(1f, 1f), Black),
    Pixel(Offset(8f, 9f), Size(1f, 1f), Black),
    Pixel(Offset(11f, 9f), Size(1f, 1f), Black),
    // eyes
    Pixel(Offset(4f, 6f), Size(2f, 2f), Black),
    Pixel(Offset(11f, 6f), Size(2f, 2f), Black),
    Pixel(Offset(4f, 6f), Size(1f, 1f), Color.White),
    Pixel(Offset(11f, 6f), Size(1f, 1f), Color.White),
    // nose
    Pixel(Offset(9f, 7f), Size(1f, 1f), Black),
)

val BodyPosition = Offset(40f, 40f)
val Body = listOf(
    Pixel(Offset(2f, 0f), Size(16f, 1f), Black),
    Pixel(Offset(18f, 1f), Size(1f, 1f), Black), // single
    Pixel(Offset(19f, 2f), Size(1f, 14f), Black),
    Pixel(Offset(18f, 16f), Size(1f, 1f), Black), // single
    Pixel(Offset(2f, 17f), Size(16f, 1f), Black),
    Pixel(Offset(1f, 16f), Size(1f, 1f), Black), // single
    Pixel(Offset(0f, 2f), Size(1f, 14f), Black),
    Pixel(Offset(1f, 1f), Size(1f, 1f), Black), // single
    // inner
    Pixel(Offset(2f, 1f), Size(16f, 16f), Beige),
    Pixel(Offset(1f, 2f), Size(18f, 14f), Beige),
    // inner
    Pixel(Offset(2f, 4f), Size(16f, 10f), Pink),
    Pixel(Offset(3f, 3f), Size(14f, 12f), Pink),
    Pixel(Offset(4f, 2f), Size(12f, 14f), Pink),
    // sprinkles
    Pixel(Offset(4f, 4f), Size(1f, 1f), Purple),
    Pixel(Offset(7f, 3f), Size(1f, 1f), Purple),
    Pixel(Offset(5f, 8f), Size(1f, 1f), Purple),
    Pixel(Offset(3f, 10f), Size(1f, 1f), Purple),
    Pixel(Offset(12f, 6f), Size(1f, 1f), Purple),
    Pixel(Offset(10f, 3f), Size(1f, 1f), Purple),
    Pixel(Offset(10f, 10f), Size(1f, 1f), Purple),
    Pixel(Offset(10f, 13f), Size(1f, 1f), Purple),
    Pixel(Offset(7f, 14f), Size(1f, 1f), Purple),
)

val TailPosition = Offset(300f, 40f)
val Tail = listOf(
    Pixel(Offset(4f, 0f), Size(2f, 1f), Black),
    Pixel(Offset(6f, 1f), Size(1f, 2f), Black),
    Pixel(Offset(6f, 2f), Size(3f, 1f), Black),
    Pixel(Offset(3f, 1f), Size(1f, 2f), Black),
    Pixel(Offset(4f, 3f), Size(1f, 1f), Black),
    Pixel(Offset(5f, 4f), Size(2f, 1f), Black),
    Pixel(Offset(7f, 5f), Size(2f, 1f), Black),
    // bg
    Pixel(Offset(4f, 1f), Size(2f, 2f), Grey),
    Pixel(Offset(5f, 3f), Size(4f, 1f), Grey),
    Pixel(Offset(7f, 4f), Size(2f, 1f), Grey),
)
val Tail2 = listOf(
    Pixel(Offset(3f, 0f), Size(4f, 1f), Black),
    Pixel(Offset(6f, 1f), Size(2f, 1f), Black),
    Pixel(Offset(7f, 2f), Size(2f, 1f), Black),
    Pixel(Offset(8f, 3f), Size(1f, 1f), Black),
    Pixel(Offset(3f, 1f), Size(1f, 2f), Black),
    Pixel(Offset(4f, 2f), Size(1f, 2f), Black),
    Pixel(Offset(5f, 3f), Size(1f, 2f), Black),
    Pixel(Offset(6f, 4f), Size(1f, 2f), Black),
    Pixel(Offset(7f, 5f), Size(2f, 1f), Black),
    Pixel(Offset(8f, 6f), Size(1f, 1f), Black),
    // bg
    Pixel(Offset(4f, 1f), Size(2f, 1f), Grey),
    Pixel(Offset(5f, 2f), Size(2f, 1f), Grey),
    Pixel(Offset(6f, 3f), Size(2f, 1f), Grey),
    Pixel(Offset(7f, 4f), Size(2f, 1f), Grey),
)
val Tail3 = listOf(
    Pixel(Offset(3f, 1f), Size(4f, 1f), Black),
    Pixel(Offset(6f, 2f), Size(3f, 1f), Black),
    Pixel(Offset(8f, 3f), Size(1f, 1f), Black),
    Pixel(Offset(2f, 2f), Size(1f, 2f), Black),
    Pixel(Offset(3f, 3f), Size(1f, 1f), Black),
    Pixel(Offset(4f, 4f), Size(4f, 1f), Black),
    Pixel(Offset(7f, 5f), Size(2f, 1f), Black),
    // bg
    Pixel(Offset(3f, 2f), Size(3f, 1f), Grey),
    Pixel(Offset(4f, 3f), Size(4f, 1f), Grey),
    Pixel(Offset(8f, 4f), Size(1f, 1f), Grey),
)
val Tail4 = listOf(
    Pixel(Offset(3f, 4f), Size(2f, 1f), Black),
    Pixel(Offset(5f, 3f), Size(4f, 1f), Black),
    Pixel(Offset(8f, 2f), Size(1f, 1f), Black),
    Pixel(Offset(3f, 5f), Size(1f, 1f), Black),
    Pixel(Offset(4f, 6f), Size(4f, 1f), Black),
    Pixel(Offset(7f, 5f), Size(2f, 1f), Black),
    // bg
    Pixel(Offset(4f, 5f), Size(3f, 1f), Grey),
    Pixel(Offset(5f, 4f), Size(4f, 1f), Grey),
)
val Tail5 = listOf(
    Pixel(Offset(3f, 5f), Size(1f, 2f), Black),
    Pixel(Offset(4f, 4f), Size(1f, 1f), Black),
    Pixel(Offset(5f, 3f), Size(2f, 1f), Black),
    Pixel(Offset(7f, 2f), Size(2f, 1f), Black),
    Pixel(Offset(4f, 7f), Size(2f, 1f), Black),
    Pixel(Offset(6f, 5f), Size(1f, 2f), Black),
    Pixel(Offset(7f, 5f), Size(2f, 1f), Black),
    // bg
    Pixel(Offset(4f, 5f), Size(2f, 2f), Grey),
    Pixel(Offset(5f, 4f), Size(4f, 1f), Grey),
    Pixel(Offset(7f, 3f), Size(2f, 1f), Grey),
)
val TailFrames = listOf(
    Tail2,
    Tail,
    Tail3,
    Tail4,
    Tail5
)

val RainBowWidth = 5f
val RainBowPosition = Offset(20f, 430f)
val RainBow = listOf(
    // first
    Pixel(Offset(0f, 0f), Size(RainBowWidth, 4f), Color.Red),
    Pixel(Offset(5f, 2f), Size(RainBowWidth, 4f), Color.Red),
    Pixel(Offset(10f, 1f), Size(8f, 4f), Color.Red),
    // second
    Pixel(Offset(0f, 4f), Size(RainBowWidth, 4f), Color.Green),
    Pixel(Offset(5f, 6f), Size(RainBowWidth, 4f), Color.Green),
    Pixel(Offset(10f, 5f), Size(8f, 4f), Color.Green),
    // third
    Pixel(Offset(0f, 8f), Size(RainBowWidth, 4f), Color.Blue),
    Pixel(Offset(5f, 10f), Size(RainBowWidth, 4f), Color.Blue),
    Pixel(Offset(10f, 9f), Size(8f, 4f), Color.Blue),
    // fourth
    Pixel(Offset(0f, 12f), Size(RainBowWidth, 4f), Color.Yellow),
    Pixel(Offset(5f, 14f), Size(RainBowWidth, 4f), Color.Yellow),
    Pixel(Offset(10f, 13f), Size(8f, 4f), Color.Yellow),
    // fifth
    Pixel(Offset(0f, 16f), Size(RainBowWidth, 4f), Color.Cyan),
    Pixel(Offset(5f, 18f), Size(RainBowWidth, 4f), Color.Cyan),
    Pixel(Offset(10f, 17f), Size(8f, 4f), Color.Cyan),
)