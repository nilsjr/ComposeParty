package de.nilsdruyen.composeparty.buttons.sticky.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.nilsdruyen.composeparty.buttons.sticky.engine.LightState

/** Grid pitch of the dot pattern in dp. The glass shader rebuilds the same grid, keep both in sync. */
const val BACKDROP_GRID: Float = 26f
private const val LIGHT_POOL_RADIUS = 320f

/**
 * Stage background in dp units: surface, a pool of light under the movable light, and a dot grid
 * centred on the stage. Call inside a DrawScope scaled to dp and pass the stage size in dp,
 * because a scaled DrawScope still reports its size in px.
 */
fun DrawScope.drawStageBackdrop(colors: BackdropColors, light: LightState?, stageSize: Size) {
    drawRect(colors.surface, size = stageSize)
    if (light != null) {
        val strength = light.strength.coerceIn(0f, 1f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(colors.lightTint.copy(alpha = colors.lightTint.alpha * strength), Color.Transparent),
                center = light.position,
                radius = LIGHT_POOL_RADIUS,
            ),
            radius = LIGHT_POOL_RADIUS,
            center = light.position,
        )
    }
    val startX = (stageSize.width / 2f) % BACKDROP_GRID
    val startY = (stageSize.height / 2f) % BACKDROP_GRID
    var y = startY
    while (y < stageSize.height) {
        var x = startX
        while (x < stageSize.width) {
            drawCircle(colors.dot, radius = 1.2f, center = Offset(x, y))
            x += BACKDROP_GRID
        }
        y += BACKDROP_GRID
    }
}
