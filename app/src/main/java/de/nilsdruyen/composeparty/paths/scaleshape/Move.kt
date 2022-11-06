package de.nilsdruyen.composeparty.paths.scaleshape

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import de.nilsdruyen.composeparty.paths.TransitionData
import de.nilsdruyen.composeparty.paths.squareSize
import de.nilsdruyen.composeparty.utils.lerp

object MoveFromTopLeftToTopRight : ScaleShapeState.Move() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val squareSize = squareSize.value
        val xOffset = lerp(0f, size.width - squareSize, transitionData.offset.x)
        return buildList {
            add(Offset(xOffset, 0f))
            add(Offset(xOffset + squareSize, 0f))
            add(Offset(xOffset + squareSize, squareSize))
            add(Offset(xOffset, squareSize))
        }
    }
}

object MoveFromTopLeftToBottomLeft : ScaleShapeState.Move() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val squareSize = squareSize.value
        val yOffset = lerp(0f, size.height - squareSize, transitionData.offset.y)
        return buildList {
            add(Offset(0f, 0f + yOffset))
            add(Offset(squareSize, 0f + yOffset))
            add(Offset(squareSize, squareSize + yOffset))
            add(Offset(0f, squareSize + yOffset))
        }
    }
}

object MoveFromTopRightToBottomRight : ScaleShapeState.Move() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val squareSize = squareSize.value
        val yOffset = lerp(0f, size.height - squareSize, transitionData.offset.y)
        return buildList {
            add(Offset(size.width - squareSize, 0f + yOffset))
            add(Offset(size.width, 0f + yOffset))
            add(Offset(size.width, squareSize + yOffset))
            add(Offset(size.width - squareSize, squareSize + yOffset))
        }
    }
}

object MoveFromTopRightToTopLeft : ScaleShapeState.Move() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val squareSize = squareSize.value
        val xOffset = lerp(0f, size.width - squareSize, transitionData.offset.x)
        return buildList {
            add(Offset(xOffset, 0f))
            add(Offset(xOffset + squareSize, 0f))
            add(Offset(xOffset + squareSize, squareSize))
            add(Offset(xOffset, squareSize))
        }
    }
}

object MoveFromBottomRightToTopRight : ScaleShapeState.Move() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val squareSize = squareSize.value
        val yOffset = lerp(0f, size.height - squareSize, transitionData.offset.y)
        return buildList {
            add(Offset(size.width - squareSize, yOffset))
            add(Offset(size.width, yOffset))
            add(Offset(size.width, yOffset + squareSize))
            add(Offset(size.width - squareSize, yOffset + squareSize))
        }
    }
}

object MoveFromBottomRightToBottomLeft : ScaleShapeState.Move() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val squareSize = squareSize.value
        val xOffset = lerp(0f, size.width - squareSize, transitionData.offset.x)
        return buildList {
            add(Offset(xOffset, size.height - squareSize))
            add(Offset(xOffset + squareSize, size.height - squareSize))
            add(Offset(xOffset + squareSize, size.height))
            add(Offset(xOffset, size.height))
        }
    }
}

object MoveFromBottomLeftToTopLeft : ScaleShapeState.Move() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val squareSize = squareSize.value
        val yOffset = lerp(0f, size.height - squareSize, transitionData.offset.y)
        return buildList {
            add(Offset(0f, 0f + yOffset))
            add(Offset(squareSize, 0f + yOffset))
            add(Offset(squareSize, squareSize + yOffset))
            add(Offset(0f, squareSize + yOffset))
        }
    }
}

object MoveFromBottomLeftToBottomRight : ScaleShapeState.Move() {
    override fun map(transitionData: TransitionData, size: Size): List<Offset> {
        val squareSize = squareSize.value
        val xOffset = lerp(0f, size.width - squareSize, transitionData.offset.x)
        return buildList {
            add(Offset(xOffset, size.height - squareSize))
            add(Offset(xOffset + squareSize, size.height - squareSize))
            add(Offset(xOffset + squareSize, size.height))
            add(Offset(xOffset, size.height))
        }
    }
}