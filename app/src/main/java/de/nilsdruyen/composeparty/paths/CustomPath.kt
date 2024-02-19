package de.nilsdruyen.composeparty.paths

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun CustomPath(modifier: Modifier = Modifier) {
    val s = 42f
    val d = 16f

    Canvas(
        modifier = modifier.background(Color.White),
    ) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(0f, s)
            lineTo(s, 0f)
            lineTo(w - s, 0f)
            lineTo(w, s)
            lineTo(w, h - s)
            lineTo(w - s, h)
            lineTo(s, h)
            lineTo(0f, h - s)
            close()
            moveTo(0f, (s / 2) - (d / 2))
            lineTo(0f, (s / 2) + (d / 2))
            lineTo((s / 2) + (d / 2), 0f)
            lineTo((s / 2) - (d / 2), 0f)
            close()
        }
        val cut = Path().apply {
            moveTo(0f, (s / 2) - (d / 2))
            lineTo(0f, (s / 2) + (d / 2))
            lineTo((s / 2) + (d / 2), 0f)
            lineTo((s / 2) - (d / 2), 0f)
            close()
        }
        drawPath(path, Color.Blue)
        drawPath(cut, Color.Red)
    }
}

@Preview
@Composable
private fun CustomPathPreview() {
    ComposePartyTheme {
        CustomPath(Modifier.size(200.dp))
    }
}