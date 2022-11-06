package de.nilsdruyen.composeparty.paths.scaleshape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import de.nilsdruyen.composeparty.paths.TransitionData
import de.nilsdruyen.composeparty.paths.multiplier
import de.nilsdruyen.composeparty.paths.squareSize
import de.nilsdruyen.composeparty.utils.lerp

object CollapseFromTopLeftToBottomRight : ScaleShapeState.Collapse() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(endSize, squareSize.value, transitionData.expand)
        val top = lerp(size.height - endSize, 0f, transitionData.expand)
        val left = lerp(size.width - endSize, 0f, transitionData.expand)

        return buildList {
            add(Offset(left, top))
            add(Offset(left + progressSize, top))
            add(Offset(size.width, size.height - endSize))
            add(Offset(size.width, size.height))
            add(Offset(size.width - endSize, size.height))
            add(Offset(left, top + progressSize))
        }
    }
}

object CollapseFromTopRightToBottomLeft : ScaleShapeState.Collapse() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(endSize, squareSize.value, transitionData.expand)
        val top = lerp(size.height - endSize, 0f, transitionData.expand)
        val right = lerp(endSize, size.width, transitionData.expand)
        return buildList {
            add(Offset(0f, size.height - endSize))
            add(Offset(right - progressSize, top))
            add(Offset(right, top))
            add(Offset(right, top + progressSize))
            add(Offset(endSize, size.height))
            add(Offset(0f, size.height))
        }
    }
}

object CollapseFromBottomRightToTopLeft : ScaleShapeState.Collapse() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(endSize, squareSize.value, transitionData.expand)
        val bottom = lerp(endSize, size.height, transitionData.expand)
        val right = lerp(endSize, size.width, transitionData.expand)
        return buildList {
            add(Offset(0f, 0f))
            add(Offset(endSize, 0f))
            add(Offset(right, bottom - progressSize))
            add(Offset(right, bottom))
            add(Offset(right - progressSize, bottom))
            add(Offset(0f, endSize))
        }
    }
}

object CollapseFromBottomLeftToTopRight : ScaleShapeState.Collapse() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val endSize = squareSize.value * multiplier
        val progressSize = lerp(endSize, squareSize.value, transitionData.expand)
        val bottom = lerp(endSize, size.height, transitionData.expand)
        val left = lerp(size.width - endSize, 0f, transitionData.expand)
        return buildList {
            add(Offset(left, bottom - progressSize))
            add(Offset(size.width - endSize, 0f))
            add(Offset(size.width, 0f))
            add(Offset(size.width, endSize))
            add(Offset(left + progressSize, bottom))
            add(Offset(left, bottom))
        }
    }
}