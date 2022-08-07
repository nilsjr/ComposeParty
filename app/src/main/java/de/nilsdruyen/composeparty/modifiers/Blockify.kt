package de.nilsdruyen.composeparty.modifiers

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawTransform
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.input.pointer.pointerInput
import de.nilsdruyen.composeparty.modifiers.BlockFilter.Companion.Lighting
import kotlinx.coroutines.launch

interface BlockFilter {

    fun ContentDrawScope.drawFront() = drawContent()
    fun ContentDrawScope.drawTop() = drawContent()
    fun ContentDrawScope.drawRight() = drawContent()

    companion object {

        fun Lighting(
            frontColor: Color = Color.Unspecified,
            topColor: Color = Color.Unspecified,
            rightColor: Color = Color.Unspecified,
            alpha: Float = 0.3f,
            blendMode: BlendMode = BlendMode.Hardlight
        ): BlockFilter = object : BlockFilter {
            override fun ContentDrawScope.drawFront() {
                drawContent()
                if (frontColor.isSpecified) {
                    drawRect(frontColor, alpha = alpha, blendMode = blendMode)
                }
            }

            override fun ContentDrawScope.drawTop() {
                drawContent()
                if (topColor.isSpecified) {
                    drawRect(topColor, alpha = alpha, blendMode = blendMode)
                }
            }

            override fun ContentDrawScope.drawRight() {
                drawContent()
                if (rightColor.isSpecified) {
                    drawRect(rightColor, alpha = alpha, blendMode = blendMode)
                }
            }
        }
    }
}

val DefaultBlockFilter: BlockFilter = Lighting(
    topColor = Color.White,
    rightColor = Color.Black
)

/**
 * Turn the modified element into a block when [enabled] is true.
 * The block can be dragged around to see different angles.
 */
fun Modifier.blockify(
    enabled: Boolean = true,
    filter: BlockFilter = DefaultBlockFilter
): Modifier = composed {
    // Animate transitions between block and no-block mode.
    val blockifiedAmount by animateFloatAsState(
        targetValue = if (enabled) 100f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessVeryLow
        )
    )
    val isBlockVisible by remember { derivedStateOf { blockifiedAmount > 50f } }

    this
        .fillMaxSize()
        .background(Color.Black)
        // The first half of the animation shows the regular view disappearing, the second half
        // shows the cube appearing.
        .graphicsLayer {
            val scale = if (isBlockVisible) {
                (blockifiedAmount - 50f) / 50
            } else {
                1f - (blockifiedAmount / 50f)
            }
            scaleX = scale
            scaleY = scale
            alpha = scale
        }
        .then(if (isBlockVisible) Modifier.cubed(filter) else Modifier)
}

private const val DefaultYDegrees: Float = 45f
private const val DefaultXDegrees: Float = 30f

/** Renders the modified element as a draggable cube. */
private fun Modifier.cubed(filter: BlockFilter) = composed {
    /** The cube's rotation around the Y axis (left-right rotation). */
    val yDegrees = remember { Animatable(DefaultYDegrees) }

    /** The cube's rotation around the X axis (up-down rotation). */
    val xDegrees = remember { Animatable(DefaultXDegrees) }
    var dragTotal: Offset by remember { mutableStateOf(Offset.Zero) }
    val scope = rememberCoroutineScope()

    this
        // Force the content to layout as a square, since we need equal sub-squares for the faces.
        .aspectRatio(1f)
        // The bottom left corner is the front face, so if the content is too small it should be
        // on the front face.
        .wrapContentSize(Alignment.BottomStart)
        .drawAsCube(filter) {
            rotateY(yDegrees.value)
            rotateX(xDegrees.value)
        }
        // Allow the cube to be rotated a bit, just for fun.
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = {
                    dragTotal = Offset.Zero
                },
                onDragEnd = {
                    scope.launch { yDegrees.animateTo(DefaultYDegrees) }
                    scope.launch { xDegrees.animateTo(DefaultXDegrees) }
                }
            ) { _, dragAmount ->
                dragTotal += dragAmount
                // Y vs X: Horizontal (x) drag corresponds to rotation around the Y axis, and vice
                // versa.
                val yDegreesDelta = DefaultYDegrees * (dragTotal.x / size.width)
                val xDegreesDelta = DefaultXDegrees * (dragTotal.y / size.height)
                scope.launch {
                    // Clamp rotation so that the faces of the cube that aren't drawn can't be
                    // brought into view.
                    yDegrees.snapTo((DefaultYDegrees - yDegreesDelta).coerceIn(0f, 90f))
                    xDegrees.snapTo((DefaultXDegrees + xDegreesDelta).coerceIn(0f, 90f))
                }
            }
        }
}

/**
 * Renders the modifier element as three faces of a cube. Assumes the content is a square, and draws
 * everything except the upper-right corner.
 */
private fun Modifier.drawAsCube(
    filter: BlockFilter,
    transform: Matrix.() -> Unit
): Modifier = drawWithCache {
    val faceSize = size.width / 2
    // Each face has its own transformation.
    val frontMatrix = Matrix().apply {
        translate(z = -faceSize / 2)
        transform()
    }
    val topMatrix = Matrix().apply {
        rotateX(-90f)
        translate(z = -faceSize / 2)
        transform()
    }
    val rightMatrix = Matrix().apply {
        rotateY(-90f)
        translate(z = -faceSize / 2)
        transform()
    }

    /** Draws a clipped sub-square of the content at the origin. */
    fun DrawTransform.extractSquare(left: Float, top: Float) {
        translate(left = -left - faceSize / 2, top = -top - faceSize / 2)
        clipRect(left = left, top = top, right = left + faceSize, bottom = top + faceSize)
    }

    onDrawWithContent {
        withTransform({
            // Scale the cube up a little since each face of the cube is only half the content size.
            scale(1.2f, 1.2f)
            // Center the cube.
            translate(size.width / 2, size.height / 2)
        }) {
            // Front face.
            withTransform({
                this.transform(frontMatrix)
                // Bottom-left sub-square.
                extractSquare(left = 0f, top = faceSize)
            }) {
                with(filter) {
                    this@onDrawWithContent.drawFront()
                }
            }

            // Top face.
            withTransform({
                this.transform(topMatrix)
                // Upper-left sub-square.
                extractSquare(left = 0f, top = 0f)
            }) {
                with(filter) {
                    this@onDrawWithContent.drawTop()
                }
            }

            // Right face.
            withTransform({
                this.transform(rightMatrix)
                // Buttom-right sub-square.
                extractSquare(left = faceSize, top = faceSize)
            }) {
                with(filter) {
                    this@onDrawWithContent.drawRight()
                }
            }
        }
    }
}