package de.nilsdruyen.composeparty.freestyle

import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DiscretePathEffect
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.ui.theme.ComposePartyTheme

@Composable
fun GooeySample() {
    val path = remember {
        Path().apply {
            addOval(Rect(Offset(150f, 150f), radius = 60f))
        }
    }
    val path2 = remember {
        Path().apply {
            addOval(Rect(Offset(270f, 150f), radius = 60f))
        }
    }

    path2.op(path, path2, PathOperation.Union)

    val pm = android.graphics.PathMeasure(path2.asAndroidPath(), true)
    val discretePathEffect = DiscretePathEffect(pm.length / 20f, 0f)
    val cornerPathEffect = CornerPathEffect(50f)

    val pathEffect = ComposePathEffect(cornerPathEffect, discretePathEffect)

    ExampleLegacyColorMatrixContent(Modifier.fillMaxSize())

//    Canvas(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//    ) {
//        drawCircle(Color.Red, radius = 60f)
//        drawCircle(
//            color = Color.Red,
//            radius = 60f,
//            center = this.center + Offset(140f, 0f)
//        )
//
//        drawPath(path, Color.Blue)
//        drawPath(
//            path2,
//            Color.Red,
//            style = Stroke(width = 10f, pathEffect = pathEffect.toComposePathEffect())
//        )
//    }
}

fun Modifier.colorMatrix(matrix: ColorMatrix) = this.then(ColorMatrixModifier(matrix))

fun alphaFilterColorMatrix() = ColorMatrix(
    floatArrayOf(
        1f, 0f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f, 0f,
        0f, 0f, 1f, 0f, 0f,
        0f, 0f, 0f, 39f, -5000f // Alpha manipulation
    )
)

class ColorMatrixModifier(private val matrix: ColorMatrix) : DrawModifier {
    override fun ContentDrawScope.draw() {
        val colorFilter = ColorFilter.colorMatrix(matrix)
        val paint = Paint().apply {
            this.colorFilter = colorFilter
        }
        val bounds = Rect(0f, 0f, size.width, size.height)
        drawIntoCanvas { canvas ->
            canvas.saveLayer(bounds, paint)
            drawContent()
            canvas.restore()
        }
    }
}

fun Modifier.colorMatrixWithContent(matrix: ColorMatrix): Modifier = this.then(
    Modifier.drawWithContent {
        val colorFilter = ColorFilter.colorMatrix(matrix)
        val paint = Paint().apply {
            this.colorFilter = colorFilter
        }
        val bounds = Rect(0f, 0f, size.width, size.height)

        drawIntoCanvas { canvas ->
            canvas.saveLayer(bounds, paint)
            drawContent() // Draw the composable content with the ColorMatrix applied
            canvas.restore()
        }
    }
)

@Composable
fun StandardColorMatrixMetaBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {

    val metaballColorMatrix = remember {
        alphaFilterColorMatrix()
    }

    Box(
        modifier = modifier
            .colorMatrix(metaballColorMatrix),
//            .colorMatrixWithContent(metaballColorMatrix)  same conception
        content = content,
    )
}

//Modifier that creates a blurred around halo
fun Modifier.outlineBlur(
    blurRadius: Dp,
    shape: Shape,
    color: Color = Color.Black
): Modifier = this.then(
    Modifier.drawWithCache {
        // Native Paint with BlurMaskFilter for the halo
        val nativePaint = android.graphics.Paint().apply {
            isAntiAlias = true
            this.color = color.toArgb()
            maskFilter = android.graphics.BlurMaskFilter(
                blurRadius.toPx(),
                android.graphics.BlurMaskFilter.Blur.NORMAL
            )

        }
        val outline = shape.createOutline(size, layoutDirection, this)
        val shapePath = Path().apply {
            addOutline(outline)
        }

        onDrawWithContent {
            drawIntoCanvas { canvas ->
                canvas.save()
                canvas.clipPath(shapePath, ClipOp.Difference)
                canvas.nativeCanvas.drawPath(shapePath.asAndroidPath(), nativePaint)
                canvas.restore()
            }
            // Draw the main content (unblurred)
            drawContent()
        }
    }
)

@Composable
fun ExampleLegacyColorMatrixContent(
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val offsetDistance = (80).dp
    val buttonOffset by animateDpAsState(
        targetValue = if (isExpanded) offsetDistance else 0.dp,
        animationSpec = tween(durationMillis = 3000)
    )

    val color = Color.Black

    StandardColorMatrixMetaBox(
        modifier = modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {

            BlurFilledTonalIconButton(
                icon = Icons.Filled.Done,
                containerColor = color,
                modifier = Modifier
                    .offset { IntOffset(x = -buttonOffset.roundToPx(), y = 0) },
                onClick = {
                    isExpanded = !isExpanded
                },
                contentDescription = null,
            )
            BlurFilledTonalIconButton(
                containerColor = color,
                modifier = Modifier
                    .offset { IntOffset(x = buttonOffset.roundToPx(), y = 0) },
                icon = Icons.Filled.Call,
                onClick = { isExpanded = !isExpanded },
                contentDescription = null
            )
        }
    }
}

@Composable
fun BlurFilledTonalIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    blurRadius: Dp = 28.dp,
    iconSize: Dp = 28.dp,
    containerColor: Color = Color.Gray,
    contentColor: Color = Color.White
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .outlineBlur(blurRadius, shape = CircleShape, color = containerColor),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = containerColor,    // Set custom container color
            contentColor = contentColor         // Set custom icon color
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Preview
@Composable
private fun GooeySamplePreview() {
    ComposePartyTheme {
        GooeySample()
    }
}