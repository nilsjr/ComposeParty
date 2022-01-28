package de.nilsdruyen.composeparty.nyancat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

const val Width = 200f
const val Height = 100f

@Composable
fun NyanCatParty() {
//    Icons.Outlined.Delete
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

@Preview
@Composable
fun NyanCat() {
    Box(Modifier.size(340.dp, 230.dp)) {
        Image(painter = rememberVectorPainter(image = nyanCat), contentDescription = null)
    }
}

val nyanCat: ImageVector = ImageVector.Builder(
    "nyanCat",
    defaultWidth = Width.dp,
    defaultHeight = Height.dp,
    viewportWidth = Width,
    viewportHeight = Height,
).path {

}.build()


// block
// h23 x w34
// 10x10 block

// head


// body
// 18 x 21
// h180 x w210
val nyanCatBody = ImageVector.Builder(
    "nyanCatBody",
    defaultWidth = Width.dp,
    defaultHeight = Height.dp,
    viewportWidth = Width,
    viewportHeight = Height,
).path {

}.build()