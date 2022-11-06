package de.nilsdruyen.composeparty.paths

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateOffset
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import de.nilsdruyen.composeparty.paths.scaleshape.*
import kotlinx.coroutines.delay
import timber.log.Timber

val squareSize = 200.dp
const val multiplier = 3
const val longDuration = 1200
const val shortDuration = 600

@Composable
fun ScaleShapeSample() {
    var shapeState by remember { mutableStateOf<ScaleShapeState>(ShrinkTopLeft) }
    val pathGeneration by remember {
        derivedStateOf { shapeState::map }
    }

    val transitionData = updateTransitionData(shapeState = shapeState)

    LaunchedEffect(shapeState) {
        delay(shapeState.duration.toLong())
        val nextState = if (shapeState is ScaleShapeState.Shrink) {
            shapeState.decideNextState()
        } else {
            shapeState.nextState()
        }
        Timber.d("next shape: $shapeState - $nextState")
        shapeState = nextState
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        drawPath(
            path = build(pathGeneration(transitionData, size)),
            color = Color.Red
        )
    }
}

class TransitionData(
//    color: State<Color>,
    offsetProgress: State<Offset>,
    sizeProgress: State<Float>,
    expandProgress: State<Float>,
) {
    //    val color by color
    val offset by offsetProgress
    val size by sizeProgress
    val expand by expandProgress
}

// Create a Transition and return its animation values.
@Composable
private fun updateTransitionData(shapeState: ScaleShapeState): TransitionData {
    val transition = updateTransition(shapeState, label = "shape")
//    val color = transition.animateColor(label = "") { state ->
//        when (state) {
//            BoxState.Collapsed -> Color.Gray
//            BoxState.Expanded -> Color.Red
//        }
//    }
    val offsetProgress = transition.animateOffset(
        transitionSpec = {
            when (targetState.type) {
                TransformState.Move, TransformState.Shrink -> tween(
                    durationMillis = shortDuration,
                    easing = FastOutSlowInEasing
                )

                TransformState.Collapse, TransformState.Expand -> tween(
                    durationMillis = shortDuration,
                    easing = FastOutSlowInEasing
                )
            }
        },
        label = "offset"
    ) { state ->
        when (state) {
            CollapseFromTopLeftToBottomRight -> BottomRight
            CollapseFromTopRightToBottomLeft -> BottomLeft
            CollapseFromBottomLeftToTopRight -> TopRight
            CollapseFromBottomRightToTopLeft -> TopLeft
            ExpandFromBottomLeftToTopRight -> TopRight
            ExpandFromBottomRightToTopLeft -> TopLeft
            ExpandFromTopLeftToBottomRight -> BottomRight
            ExpandFromTopRightToBottomLeft -> BottomLeft
            MoveFromBottomLeftToBottomRight -> BottomRight
            MoveFromBottomLeftToTopLeft -> TopLeft
            MoveFromBottomRightToBottomLeft -> BottomLeft
            MoveFromBottomRightToTopRight -> TopRight
            MoveFromTopLeftToBottomLeft -> BottomLeft
            MoveFromTopLeftToTopRight -> TopRight
            MoveFromTopRightToBottomRight -> BottomRight
            MoveFromTopRightToTopLeft -> TopLeft
            ShrinkBottomLeft -> BottomLeft
            ShrinkBottomRight -> BottomRight
            ShrinkTopLeft -> TopLeft
            ShrinkTopRight -> TopRight
            else -> TopRight
        }
    }
    val sizeProgress = transition.animateFloat(
        transitionSpec = {
            when {
                initialState.type == TransformState.Collapse && targetState.type == TransformState.Shrink -> tween(
                    durationMillis = shortDuration,
                    easing = FastOutSlowInEasing
                )

                else -> tween(durationMillis = longDuration, easing = FastOutSlowInEasing)
            }
        },
        label = "size"
    ) { state ->
        when (state) {
            is ScaleShapeState.Collapse -> 1f
            is ScaleShapeState.Expand -> 1f
            is ScaleShapeState.Move -> 0f
            is ScaleShapeState.Shrink -> 0f
        }
    }
    val expandProgress = transition.animateFloat(
        transitionSpec = {
            when {
                initialState.type == TransformState.Collapse && targetState.type == TransformState.Shrink -> tween(
                    durationMillis = shortDuration,
                    easing = FastOutSlowInEasing
                )

                else -> tween(durationMillis = longDuration, easing = FastOutSlowInEasing)
            }
        },
        label = "expand"
    ) { state ->
        when (state) {
            is ScaleShapeState.Collapse -> 0f
            is ScaleShapeState.Expand -> 1f
            is ScaleShapeState.Move -> 0f
            is ScaleShapeState.Shrink -> 0f
        }
    }
    return remember(transition) {
        TransitionData(
//            color = color,
            offsetProgress = offsetProgress,
            sizeProgress = sizeProgress,
            expandProgress = expandProgress
        )
    }
}

internal fun build(data: List<Offset>): Path = Path().apply {
    reset()
    data.forEachIndexed { index, point ->
        if (index == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
    }
    close()
}

fun ScaleShapeState.decideNextState(): ScaleShapeState = when (this) {
    is ShrinkTopRight -> listOf(MoveFromTopRightToTopLeft, MoveFromTopRightToBottomRight).random()
    is ShrinkTopLeft -> listOf(MoveFromTopLeftToBottomLeft, MoveFromTopLeftToTopRight).random()
    is ShrinkBottomRight -> listOf(
        MoveFromBottomRightToTopRight, MoveFromBottomRightToBottomLeft
    ).random()

    is ShrinkBottomLeft -> listOf(
        MoveFromBottomLeftToTopLeft, MoveFromBottomLeftToBottomRight
    ).random()

    else -> throw IllegalStateException("wrong state")
}

fun ScaleShapeState.nextState(): ScaleShapeState = when (this) {
    MoveFromTopLeftToBottomLeft -> ExpandFromBottomLeftToTopRight
    MoveFromTopLeftToTopRight -> ExpandFromTopRightToBottomLeft
    MoveFromTopRightToBottomRight -> ExpandFromBottomRightToTopLeft
    MoveFromTopRightToTopLeft -> ExpandFromTopLeftToBottomRight
    MoveFromBottomRightToBottomLeft -> ExpandFromBottomLeftToTopRight
    MoveFromBottomRightToTopRight -> ExpandFromTopRightToBottomLeft
    MoveFromBottomLeftToBottomRight -> ExpandFromBottomRightToTopLeft
    MoveFromBottomLeftToTopLeft -> ExpandFromTopLeftToBottomRight
    ExpandFromTopLeftToBottomRight -> CollapseFromTopLeftToBottomRight
    ExpandFromTopRightToBottomLeft -> CollapseFromTopRightToBottomLeft
    ExpandFromBottomLeftToTopRight -> CollapseFromBottomLeftToTopRight
    ExpandFromBottomRightToTopLeft -> CollapseFromBottomRightToTopLeft
    CollapseFromBottomRightToTopLeft -> ShrinkTopLeft
    CollapseFromBottomLeftToTopRight -> ShrinkTopRight
    CollapseFromTopLeftToBottomRight -> ShrinkBottomRight
    CollapseFromTopRightToBottomLeft -> ShrinkBottomLeft
    else -> throw IllegalStateException()
}
