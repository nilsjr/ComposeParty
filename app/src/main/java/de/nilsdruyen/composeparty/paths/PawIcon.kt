package de.nilsdruyen.composeparty.paths

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.Group
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun PawIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    val solidColor = SolidColor(color)
    val parts = listOf(
        "M-0.88 -4.85 C-11.24,-1.32 -9.99,5.65 -11.9,12.35 C-12.79,15.43 -24.91,25.57 -17.64,35.27 C-11.13,43.95 4.34,33.27 8.82,32.85 C13.45,32.41 28,38.8 33.51,26.45 C38.45,15.38 24.69,11.9 19.4,7.72 C14.11,3.53 12.59,-9.43 -0.88,-4.85c",
        "M-43.25 -8 C-45.75,-5 -46,4.5 -41.75,10.25 C-37.5,16 -30.75,19.75 -23.5,17.75 C-16.25,15.75 -16.25,5.25 -22.75,-2.75 C-29.79,-11.41 -38.7,-13.46 -43.25,-8c",
        "M-20.75 -40.75 C-29,-39.25 -30.75,-26.25 -26.5,-17 C-22.25,-7.75 -13.73,-7.06 -8.5,-10.25 C-2.75,-13.75 -5.5,-26.75 -7.25,-30.5 C-9,-34.25 -12.5,-42.25 -20.75,-40.75c",
        "M-20.75 -40.75 C-29,-39.25 -30.75,-26.25 -26.5,-17 C-22.25,-7.75 -13.73,-7.06 -8.5,-10.25 C-2.75,-13.75 -5.5,-26.75 -7.25,-30.5 C-9,-34.25 -12.5,-42.25 -20.75,-40.75c",
        "M-43.25 -8 C-45.75,-5 -46,4.5 -41.75,10.25 C-37.5,16 -30.75,19.75 -23.5,17.75 C-16.25,15.75 -16.25,5.25 -22.75,-2.75 C-29.79,-11.41 -38.7,-13.46 -43.25,-8c",
    )
    val drawParts = remember { parts.map { addPathNodes(it) } }

    Image(
        modifier = Modifier
            .size(100.dp)
            .then(modifier),
        painter = rememberVectorPainter(
            defaultWidth = 104.dp,
            defaultHeight = 104.dp,
            viewportWidth = 104f,
            viewportHeight = 104f,
            autoMirror = false,
        ) { vw, vh ->
            Group {
                Group(
                    name = "01",
                    translationX = 52f,
                    translationY = 52f,
                ) {
                    Path(
                        pathData = drawParts[0],
                        stroke = solidColor,
                        strokeLineWidth = 4f
                    )
                }
                Group(
                    name = "paw01",
                    scaleX = 0.959f,
                    scaleY = 1f,
                    translationX = 49.278f - 3,
                    translationY = 50.879f - 5,
                ) {
                    Path(
                        pathData = drawParts[1],
                        stroke = solidColor,
                        strokeLineWidth = 4f,
                    )
                }
                Group(
                    name = "paw02",
                    scaleX = 0.94136f,
                    scaleY = 1f,
                    translationX = 50.5f,
                    translationY = 48.335f,
                ) {
                    Path(
                        pathData = drawParts[2],
                        stroke = solidColor,
                        strokeLineWidth = 4f
                    )
                }
                Group(
                    name = "paw03",
                    rotation = -19.37f,
                    scaleX = -0.94136f,
                    scaleY = 1f,
                    translationX = 55.597f,
                    translationY = 48.182f,
                ) {
                    Path(
                        pathData = drawParts[3],
                        stroke = solidColor,
                        strokeLineWidth = 4f
                    )
                }
                Group(
                    name = "paw04",
                    rotation = -25.753f,
                    scaleX = -0.9197799f,
                    scaleY = 1f,
                    translationX = 59.041f,
                    translationY = 53.054f,
                ) {
                    Path(
                        pathData = drawParts[4],
                        stroke = solidColor,
                        pathFillType = PathFillType.NonZero,
                        strokeLineWidth = 4f,
                        strokeAlpha = 1f,
                        strokeLineCap = StrokeCap.Round,
                        strokeLineJoin = StrokeJoin.Round,
                    )
                }
            }
        },
        contentDescription = null,
    )
}

@Preview(showBackground = true, backgroundColor = 0xfff)
@Composable
fun PreviewPawIcon() {
    ComposePartyTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            PawIcon(
                modifier = Modifier.scale(.5f).align(Alignment.Center),
                color = Color.DarkGray,
            )
        }
    }
}