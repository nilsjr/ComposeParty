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
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        return Outline.Generic(path)
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