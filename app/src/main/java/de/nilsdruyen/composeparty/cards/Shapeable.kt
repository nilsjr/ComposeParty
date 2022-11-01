package de.nilsdruyen.composeparty.cards

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class Shapeable(val decoration: ShapeDecoration) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val topStartSize = decoration.topStart.size.value * density.density
        val topEndSize = decoration.topEnd.size.value * density.density
        val bottomEndSize = decoration.bottomEnd.size.value * density.density
        val bottomStartSize = decoration.bottomStart.size.value * density.density

        val path = Path().apply {
            applyTopStartPath(decoration.topStart.type, topStartSize)
            applyTopEndPath(decoration.topEnd.type, topEndSize, size)
            applyBottomEndPath(decoration.bottomEnd.type, bottomEndSize, size)
            applyBottomStartPath(decoration.bottomStart.type, bottomStartSize, size)
            close()
        }
        return Outline.Generic(path)
    }
}

fun Path.applyTopStartPath(type: CornerType, shapeSize: Float) {
    when (type) {
        CornerType.CUT -> {
            moveTo(0f, shapeSize)
            lineTo(shapeSize, 0f)
        }
        CornerType.ROUND -> {
            moveTo(0f, shapeSize)
            cubicTo(0f, 0f, 0f, 0f, shapeSize, 0f)
        }
        CornerType.NONE -> {
            moveTo(0f, 0f)
        }
    }
}
fun Path.applyTopEndPath(type: CornerType, shapeSize: Float, size: Size) {
    when (type) {
        CornerType.CUT -> {
            lineTo(size.width - shapeSize, 0f)
            lineTo(size.width, shapeSize)
        }
        CornerType.ROUND -> {
            lineTo(size.width - shapeSize, 0f)
            cubicTo(size.width, 0f, size.width, 0f, size.width, shapeSize)
        }
        CornerType.NONE -> {
            lineTo(size.width, 0f)
        }
    }
}

fun Path.applyBottomEndPath(type: CornerType, shapeSize: Float, size: Size) {
    when (type) {
        CornerType.CUT -> {
            lineTo(size.width, size.height - shapeSize)
            lineTo(size.width - shapeSize, size.height)
        }
        CornerType.ROUND -> {
            lineTo(size.width, size.height - shapeSize)
            cubicTo(size.width, size.height, size.width, size.height, size.width - shapeSize, size.height)
        }
        CornerType.NONE -> {
            lineTo(size.width, size.height)
        }
    }
}

fun Path.applyBottomStartPath(type: CornerType, shapeSize: Float, size: Size) {
    when (type) {
        CornerType.CUT -> {
            lineTo(shapeSize, size.height)
            lineTo(0f, size.height - shapeSize)
        }
        CornerType.ROUND -> {
            lineTo(shapeSize, size.height)
            cubicTo(0f, size.height, 0f, size.height, 0f, size.height - shapeSize)
        }
        CornerType.NONE -> {
           lineTo(0f, size.height)
        }
    }
}

data class ShapeDecoration(
    val topStart: ShapeCorner = ShapeCorner(),
    val topEnd: ShapeCorner = ShapeCorner(),
    val bottomEnd: ShapeCorner = ShapeCorner(),
    val bottomStart: ShapeCorner = ShapeCorner(),
)

data class ShapeCorner(
    val type: CornerType = CornerType.NONE,
    val size: Dp = 0.dp,
)

enum class CornerType {
    CUT, ROUND, NONE
}