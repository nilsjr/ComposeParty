package de.nilsdruyen.composeparty.paths.scaleshape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import de.nilsdruyen.composeparty.paths.TransitionData
import de.nilsdruyen.composeparty.paths.longDuration
import de.nilsdruyen.composeparty.paths.shortDuration

val TopLeft = Offset(0f, 0f)
val TopRight = Offset(1f, 0f)
val BottomRight = Offset(1f, 1f)
val BottomLeft = Offset(0f, 1f)

sealed class ScaleShapeState constructor(
    val type: TransformState,
    val duration: Int = longDuration,
) {

    abstract fun map(transitionData: TransitionData, size: Size): List<Offset>

    abstract class Shrink : ScaleShapeState(TransformState.Shrink, shortDuration)
    abstract class Move : ScaleShapeState(TransformState.Move, shortDuration)
    abstract class Collapse : ScaleShapeState(TransformState.Collapse)
    abstract class Expand : ScaleShapeState(TransformState.Expand)
}

enum class TransformState {
    Move, Shrink, Collapse, Expand
}