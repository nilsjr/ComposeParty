package de.nilsdruyen.composeparty.nyancat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
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

// block
// h23 x w34
// 10x10 block
// body
// 18 x 21
// h180 x w210

@Composable
fun NyanCatParty() {
    Box(
        modifier = Modifier
            .size(600.dp, 400.dp)
            .background(Color(0xFFFFFFFF))
    ) {
        NyanCat()
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
fun NyanCat() {
    Box {
        Canvas(
            modifier = Modifier
                .background(Color.White)
                .size(340.dp, 230.dp)
                .align(Alignment.Center)
        ) {
            draw(Body, BodyPosition)
            draw(Head, HeadPosition)
            drawRect(Color.LightGray, Offset(300f, 40f), Size(10f, 10f).times(10f))
            TailFrames.forEachIndexed { index, item -> draw(item, TailPosition.plus(Offset(0f, 100f * index))) }
            draw(RainBow, RainBowPosition)
        }
    }
}

fun DrawScope.draw(list: List<Part>, offset: Offset) {
    list.forEach {
        drawRect(
            it.color,
            topLeft = it.rect.topLeft.times(PxSize).plus(offset),
            size = it.rect.size.times(PxSize)
        )
    }
}

const val PxSize = 10f
const val PxWidth = 20f
const val PxHeight = 20f

data class Part(val rect: Rect, val color: Color)

val HeadPosition = Offset(40f, 250f)
val Head = listOf(
    // border
    Part(Rect(Offset(2f, 0f), Size(2f, 1f)), Black),
    Part(Rect(Offset(4f, 1f), Size(1f, 1f)), Black),
    Part(Rect(Offset(5f, 2f), Size(1f, 1f)), Black),
    Part(Rect(Offset(6f, 3f), Size(4f, 1f)), Black),
    Part(Rect(Offset(10f, 2f), Size(1f, 1f)), Black),
    Part(Rect(Offset(11f, 1f), Size(1f, 1f)), Black),
    Part(Rect(Offset(12f, 0f), Size(2f, 1f)), Black),
    Part(Rect(Offset(14f, 1f), Size(1f, 4f)), Black),
    Part(Rect(Offset(15f, 5f), Size(1f, 5f)), Black),
    Part(Rect(Offset(14f, 10f), Size(1f, 1f)), Black),
    Part(Rect(Offset(13f, 11f), Size(1f, 1f)), Black),
    Part(Rect(Offset(3f, 12f), Size(10f, 1f)), Black),
    Part(Rect(Offset(2f, 11f), Size(1f, 1f)), Black),
    Part(Rect(Offset(1f, 10f), Size(1f, 1f)), Black),
    Part(Rect(Offset(0f, 5f), Size(1f, 5f)), Black),
    Part(Rect(Offset(1f, 1f), Size(1f, 4f)), Black),
    // bg
    Part(Rect(Offset(2f, 1f), Size(2f, 4f)), Grey),
    Part(Rect(Offset(4f, 2f), Size(1f, 3f)), Grey),
    Part(Rect(Offset(5f, 3f), Size(1f, 1f)), Grey),
    Part(Rect(Offset(5f, 4f), Size(9f, 1f)), Grey),
    Part(Rect(Offset(10f, 3f), Size(4f, 1f)), Grey),
    Part(Rect(Offset(11f, 2f), Size(3f, 1f)), Grey),
    Part(Rect(Offset(12f, 1f), Size(2f, 1f)), Grey),
    Part(Rect(Offset(1f, 5f), Size(14f, 5f)), Grey),
    Part(Rect(Offset(2f, 10f), Size(12f, 1f)), Grey),
    Part(Rect(Offset(3f, 11f), Size(10f, 1f)), Grey),
    // cheek
    Part(Rect(Offset(2f, 8f), Size(2f, 2f)), Rosa),
    Part(Rect(Offset(13f, 8f), Size(2f, 2f)), Rosa),
    // mouth
    Part(Rect(Offset(5f, 10f), Size(7f, 1f)), Black),
    Part(Rect(Offset(5f, 9f), Size(1f, 1f)), Black),
    Part(Rect(Offset(8f, 9f), Size(1f, 1f)), Black),
    Part(Rect(Offset(11f, 9f), Size(1f, 1f)), Black),
    // eyes
    Part(Rect(Offset(4f, 6f), Size(2f, 2f)), Black),
    Part(Rect(Offset(11f, 6f), Size(2f, 2f)), Black),
    Part(Rect(Offset(4f, 6f), Size(1f, 1f)), Color.White),
    Part(Rect(Offset(11f, 6f), Size(1f, 1f)), Color.White),
    // nose
    Part(Rect(Offset(9f, 7f), Size(1f, 1f)), Black),
)

val BodyPosition = Offset(40f, 40f)
val Body: List<Part> = listOf(
    Part(Rect(Offset(2f, 0f), Size(16f, 1f)), Black),
    Part(Rect(Offset(18f, 1f), Size(1f, 1f)), Black), // single
    Part(Rect(Offset(19f, 2f), Size(1f, 14f)), Black),
    Part(Rect(Offset(18f, 16f), Size(1f, 1f)), Black), // single
    Part(Rect(Offset(2f, 17f), Size(16f, 1f)), Black),
    Part(Rect(Offset(1f, 16f), Size(1f, 1f)), Black), // single
    Part(Rect(Offset(0f, 2f), Size(1f, 14f)), Black),
    Part(Rect(Offset(1f, 1f), Size(1f, 1f)), Black), // single
    // inner
    Part(Rect(Offset(2f, 1f), Size(16f, 16f)), Beige),
    Part(Rect(Offset(1f, 2f), Size(18f, 14f)), Beige),
    // inner
    Part(Rect(Offset(2f, 4f), Size(16f, 10f)), Pink),
    Part(Rect(Offset(3f, 3f), Size(14f, 12f)), Pink),
    Part(Rect(Offset(4f, 2f), Size(12f, 14f)), Pink),
    // sprinkles
    Part(Rect(Offset(4f, 4f), Size(1f, 1f)), Purple),
    Part(Rect(Offset(7f, 3f), Size(1f, 1f)), Purple),
    Part(Rect(Offset(5f, 8f), Size(1f, 1f)), Purple),
    Part(Rect(Offset(3f, 10f), Size(1f, 1f)), Purple),
    Part(Rect(Offset(12f, 6f), Size(1f, 1f)), Purple),
    Part(Rect(Offset(10f, 3f), Size(1f, 1f)), Purple),
    Part(Rect(Offset(10f, 10f), Size(1f, 1f)), Purple),
    Part(Rect(Offset(10f, 13f), Size(1f, 1f)), Purple),
    Part(Rect(Offset(7f, 14f), Size(1f, 1f)), Purple),
)

val TailPosition = Offset(300f, 40f)
val Tail = listOf(
    Part(Rect(Offset(1f, 0f), Size(2f, 1f)), Black),
    Part(Rect(Offset(3f, 1f), Size(1f, 2f)), Black),
    Part(Rect(Offset(3f, 2f), Size(3f, 1f)), Black),
    Part(Rect(Offset(0f, 1f), Size(1f, 2f)), Black),
    Part(Rect(Offset(1f, 3f), Size(1f, 1f)), Black),
    Part(Rect(Offset(2f, 4f), Size(2f, 1f)), Black),
    Part(Rect(Offset(4f, 5f), Size(2f, 1f)), Black),
    // bg
    Part(Rect(Offset(1f, 1f), Size(2f, 2f)), Grey),
    Part(Rect(Offset(2f, 3f), Size(4f, 1f)), Grey),
    Part(Rect(Offset(4f, 4f), Size(2f, 1f)), Grey),
)
val Tail2 = listOf(
    Part(Rect(Offset(1f, 0f), Size(2f, 1f)), Black),
)
val TailFrames = listOf(Tail, Tail)

val RainBowWidth = 6f
val RainBowPosition = Offset(300f, 300f)
val RainBow = listOf(
    // first
    Part(Rect(Offset(0f, 0f), Size(RainBowWidth, 4f)), Color.Red),
    Part(Rect(Offset(5f, 2f), Size(RainBowWidth, 4f)), Color.Red),
    Part(Rect(Offset(10f, 1f), Size(8f, 4f)), Color.Red),
    // second
    Part(Rect(Offset(0f, 4f), Size(RainBowWidth, 4f)), Color.Green),
    Part(Rect(Offset(5f, 6f), Size(RainBowWidth, 4f)), Color.Green),
    Part(Rect(Offset(10f, 5f), Size(8f, 4f)), Color.Green),
    // third
    Part(Rect(Offset(0f, 8f), Size(RainBowWidth, 4f)), Color.Blue),
    Part(Rect(Offset(5f, 10f), Size(RainBowWidth, 4f)), Color.Blue),
    Part(Rect(Offset(10f, 9f), Size(8f, 4f)), Color.Blue),
    // fourth
    Part(Rect(Offset(0f, 12f), Size(RainBowWidth, 4f)), Color.Yellow),
    Part(Rect(Offset(5f, 14f), Size(RainBowWidth, 4f)), Color.Yellow),
    Part(Rect(Offset(10f, 13f), Size(8f, 4f)), Color.Yellow),
    // fifth
    Part(Rect(Offset(0f, 16f), Size(RainBowWidth, 4f)), Color.Cyan),
    Part(Rect(Offset(5f, 18f), Size(RainBowWidth, 4f)), Color.Cyan),
    Part(Rect(Offset(10f, 17f), Size(8f, 4f)), Color.Cyan),
)