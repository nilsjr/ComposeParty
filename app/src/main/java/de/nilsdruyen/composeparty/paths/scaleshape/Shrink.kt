package de.nilsdruyen.composeparty.paths.scaleshape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import de.nilsdruyen.composeparty.paths.TransitionData
import de.nilsdruyen.composeparty.paths.multiplier
import de.nilsdruyen.composeparty.paths.squareSize
import de.nilsdruyen.composeparty.utils.lerp

object ShrinkTopLeft : ScaleShapeState.Shrink() {

    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(squareSize.value, endSize, transitionData.size)
        return buildList {
            add(Offset(0f, 0f))
            add(Offset(progressSize, 0f))
            add(Offset(progressSize, progressSize))
            add(Offset(0f, progressSize))
        }
    }
}

object ShrinkTopRight : ScaleShapeState.Shrink() {

    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(squareSize.value, endSize, transitionData.size)
        return buildList {
            add(Offset(size.width - progressSize, 0f))
            add(Offset(size.width, 0f))
            add(Offset(size.width, progressSize))
            add(Offset(size.width - progressSize, progressSize))
        }
    }
}

object ShrinkBottomRight : ScaleShapeState.Shrink() {

    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(squareSize.value, endSize, transitionData.size)
        return buildList {
            add(Offset(size.width - progressSize, size.height - progressSize))
            add(Offset(size.width, size.height - progressSize))
            add(Offset(size.width, size.height))
            add(Offset(size.width - progressSize, size.height))
        }
    }
}

object ShrinkBottomLeft : ScaleShapeState.Shrink() {

    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(squareSize.value, endSize, transitionData.size)
        return buildList {
            add(Offset(0f, size.height - progressSize))
            add(Offset(progressSize, size.height - progressSize))
            add(Offset(progressSize, size.height))
            add(Offset(0f, size.height))
        }
    }
}