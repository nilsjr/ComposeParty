package de.nilsdruyen.composeparty.paths

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PawIcon() {
    val parts = listOf(
        " M-0.88 -4.85 C-11.24,-1.32 -9.99,5.65 -11.9,12.35 C-12.79,15.43 -24.91,25.57 -17.64,35.27 C-11.13,43.95 4.34,33.27 8.82,32.85 C13.45,32.41 28,38.8 33.51,26.45 C38.45,15.38 24.69,11.9 19.4,7.72 C14.11,3.53 12.59,-9.43 -0.88,-4.85c ",
        " M-0.88 -4.85 C-11.24,-1.32 -9.99,5.65 -11.9,12.35 C-12.79,15.43 -24.91,25.57 -17.64,35.27 C-11.13,43.95 4.34,33.27 8.82,32.85 C13.45,32.41 28,38.8 33.51,26.45 C38.45,15.38 24.69,11.9 19.4,7.72 C14.11,3.53 12.59,-9.43 -0.88,-4.85c ",
        " M-43.25 -8 C-45.75,-5 -46,4.5 -41.75,10.25 C-37.5,16 -30.75,19.75 -23.5,17.75 C-16.25,15.75 -16.25,5.25 -22.75,-2.75 C-29.79,-11.41 -38.7,-13.46 -43.25,-8c ",
        " M-20.75 -40.75 C-29,-39.25 -30.75,-26.25 -26.5,-17 C-22.25,-7.75 -13.73,-7.06 -8.5,-10.25 C-2.75,-13.75 -5.5,-26.75 -7.25,-30.5 C-9,-34.25 -12.5,-42.25 -20.75,-40.75c ",
        " M-20.75 -40.75 C-29,-39.25 -30.75,-26.25 -26.5,-17 C-22.25,-7.75 -13.73,-7.06 -8.5,-10.25 C-2.75,-13.75 -5.5,-26.75 -7.25,-30.5 C-9,-34.25 -12.5,-42.25 -20.75,-40.75c ",
        " M-43.25 -8 C-45.75,-5 -46,4.5 -41.75,10.25 C-37.5,16 -30.75,19.75 -23.5,17.75 C-16.25,15.75 -16.25,5.25 -22.75,-2.75 C-29.79,-11.41 -38.7,-13.46 -43.25,-8c ",
    )

    val drawParts = remember { parts.map { addPathNodes(it) } }
//    Icons.Filled.Delete

    Image(
        painter = rememberVectorPainter(
            defaultWidth = 104.dp,
            defaultHeight = 104.dp,
            viewportWidth = 104f,
            viewportHeight = 104f,
        ) { vw, vh ->
            Path(
                pathData = remember { addPathNodes("h $vw v $vh h -$vw v -$vh") },
                fill = SolidColor(Color.White),
            )

            drawParts.forEach {

                Path(

                    pathData = it,
                    stroke = SolidColor(Color.Red),
                    strokeLineWidth = 2f,
                )
            }
        },
        contentDescription = null,
        modifier = Modifier.size(120.dp),
    )
}

const val Part01 = """
    M-0.88 -4.85 
    C-11.24,-1.32 -9.99,5.65 -11.9,12.35 
    C-12.79,15.43 -24.91,25.57 -17.64,35.27 
    C-11.13,43.95 4.34,33.27 8.82,32.85 
    C13.45,32.41 28,38.8 33.51,26.45 
    C38.45,15.38 24.69,11.9 19.4,7.72 
    C14.11,3.53 12.59,-9.43 -0.88,-4.85c 
"""