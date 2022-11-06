package de.nilsdruyen.composeparty.paths.scaleshape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import de.nilsdruyen.composeparty.paths.TransitionData
import de.nilsdruyen.composeparty.paths.multiplier
import de.nilsdruyen.composeparty.paths.squareSize
import de.nilsdruyen.composeparty.utils.lerp

object ExpandFromTopLeftToBottomRight : ScaleShapeState.Expand() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(squareSize.value, endSize, transitionData.expand)
        val bottom = lerp(squareSize.value, size.height, transitionData.expand)
        val right = lerp(squareSize.value, size.width, transitionData.expand)
        return buildList {
            add(Offset(0f, 0f))
            add(Offset(squareSize.value, 0f))
            add(Offset(right, bottom - progressSize))
            add(Offset(right, bottom))
            add(Offset(right - progressSize, bottom))
            add(Offset(0f, squareSize.value))
        }
    }
}

object ExpandFromTopRightToBottomLeft : ScaleShapeState.Expand() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(squareSize.value, endSize, transitionData.expand)
        val bottom = lerp(squareSize.value, size.height, transitionData.expand)
        val left = lerp(size.width - squareSize.value, 0f, transitionData.expand)
        return buildList {
            add(Offset(left, bottom - progressSize))
            add(Offset(size.width - squareSize.value, 0f))
            add(Offset(size.width, 0f))
            add(Offset(size.width, squareSize.value))
            add(Offset(left + progressSize, bottom))
            add(Offset(left, bottom))
        }
    }
}

object ExpandFromBottomLeftToTopRight : ScaleShapeState.Expand() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(squareSize.value, endSize, transitionData.expand)
        val topStart = size.height - squareSize.value
        val top = lerp(topStart, 0f, transitionData.expand)
        val right = lerp(squareSize.value, size.width, transitionData.expand)
        return buildList {
            add(Offset(0f, topStart))
            add(Offset(right - progressSize, top))
            add(Offset(right, top))
            add(Offset(right, top + progressSize))
            add(Offset(squareSize.value, size.height))
            add(Offset(0f, size.height))
        }
    }
}

object ExpandFromBottomRightToTopLeft : ScaleShapeState.Expand() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(squareSize.value, endSize, transitionData.expand)
        val top = lerp(size.height - squareSize.value, 0f, transitionData.expand)
        val left = lerp(size.width - squareSize.value, 0f, transitionData.expand)
        return buildList {
            add(Offset(left, top))
            add(Offset(left + progressSize, top))
            add(Offset(size.width, size.height - squareSize.value))
            add(Offset(size.width, size.height))
            add(Offset(size.width - squareSize.value, size.height))
            add(Offset(left, top + progressSize))
        }
    }
}