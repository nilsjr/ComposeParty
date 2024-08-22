package de.nilsdruyen.composeparty.freestyle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CanvasSample() {
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {

        }
        RotateText()
    }
}

@Composable
fun RotateText() {
    val textMeasurer = rememberTextMeasurer()

    val statusBarInsets = WindowInsets.statusBars.asPaddingValues()
    val statusBarPadding = statusBarInsets.calculateBottomPadding()
    val additionalPadding = with(LocalDensity.current) {
        (statusBarPadding + 12.dp).toPx()
    }
    val cornerValue = 12f

    Canvas(modifier = Modifier.size(150.dp, 80.dp)) {
        val text = textMeasurer.measure(
            AnnotatedString("Hello"),
            TextStyle(fontWeight = FontWeight.Bold, color = Color.White)
        )
        val textStart = text.size.width / 2
        val widthVertical = text.size.height.toFloat() * 1.1f

        val path2 = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, statusBarInsets.calculateBottomPadding().toPx())
            quadraticTo(
                x1 = size.width,
                y1 = additionalPadding,
                x2 = size.width - cornerValue,
                y2 = additionalPadding
            )
            lineTo(widthVertical + cornerValue, additionalPadding)
            quadraticTo(
                x1 = widthVertical,
                y1 = additionalPadding,
                x2 = widthVertical,
                y2 = additionalPadding + cornerValue
            )
            lineTo(widthVertical, size.height - cornerValue)
            quadraticTo(
                x1 = widthVertical,
                y1 = size.height,
                x2 = widthVertical - cornerValue,
                y2 = size.height
            )
            lineTo(0f, size.height)
            close()
        }
        drawPath(
            path = path2,
            color = Color.Blue,
        )
        translate(top = size.height) {
            rotate(-90f, Offset.Zero) {
                drawText(text, topLeft = Offset(textStart.toFloat(), 0f))
            }
        }
    }
}