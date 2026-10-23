package de.nilsdruyen.composeparty.buttons.sticky.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import de.nilsdruyen.composeparty.buttons.sticky.effects.OverlayColors
import de.nilsdruyen.composeparty.buttons.sticky.render.BackdropColors

/** Design tokens, mirrored from the web version's CSS custom properties. */
@Immutable
data class LabColors(
    val ground: Color,
    val surface: Color,
    val ink: Color,
    val inkSoft: Color,
    val line: Color,
    val accent: Color,
    val dot: Color,
    val lightTint: Color,
    /** Fill of a solid (non-liquid) button. */
    val button: Color,
    /** What a solid button heats up to. */
    val warn: Color,
) {
    val backdrop: BackdropColors get() = BackdropColors(surface = surface, dot = dot, lightTint = lightTint)
    val overlay: OverlayColors get() = OverlayColors(ink = ink, accent = accent)

    companion object {
        val Light = LabColors(
            ground = Color(0xFFE3E4E9),
            surface = Color(0xFFD6D8DF),
            ink = Color(0xFF111218),
            inkSoft = Color(0xFF4B4E5A),
            line = Color(0x24111218),
            accent = Color(0xFF3A35DC),
            dot = Color(0x21111218),
            lightTint = Color.White.copy(alpha = 0.85f),
            button = Color(0xFF0B0C10),
            warn = Color(0xFFD33F27),
        )

        val Dark = LabColors(
            ground = Color(0xFF101115),
            surface = Color(0xFF1B1D24),
            ink = Color(0xFFEBECF0),
            inkSoft = Color(0xFF9A9EAC),
            line = Color(0x24EBECF0),
            accent = Color(0xFF938EFF),
            dot = Color(0x17EBECF0),
            lightTint = Color(0x33BEC4FF),
            button = Color(0xFFDEDCD5),
            warn = Color(0xFFFF6A4D),
        )
    }
}

val LocalLabColors = staticCompositionLocalOf { LabColors.Light }
