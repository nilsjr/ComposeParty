package de.nilsdruyen.composeparty.buttons.sticky.effects

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageFrame
import de.nilsdruyen.composeparty.buttons.sticky.render.LiquidMaterial

/** Which canvas an effect paints on. */
enum class EffectLayer {
    /** Effect paints a coverage mask; the liquid shader lights it with [EffectMeta.material]. */
    Liquid,

    /** Effect paints final colours directly on the overlay. */
    Flat,
}

/** How the button label is typeset. */
enum class LabelStyle { Regular, Pixel }

/** Static description of an effect, used by the tabs, the button and the notes. */
data class EffectMeta(
    val id: String,
    val name: String,
    val layer: EffectLayer,
    val material: LiquidMaterial?,
    val buttonLabel: String,
    val labelColor: Color,
    val labelStyle: LabelStyle = LabelStyle.Regular,
    /** True when the stage draws a regular pill button that the effect poses via [ButtonEffect.pose]. */
    val isSolidButton: Boolean = false,
    val description: String,
    val technique: String,
)

/** Theme colours an effect may use for flat overlay drawing. */
data class OverlayColors(val ink: Color, val accent: Color)

/**
 * Where the stage draws the button this frame. Offsets are dp, [labelOffset] is relative to the
 * button, [rotation] is degrees and [heat] (0..1) tints a solid button toward the warning colour.
 */
data class ButtonPose(
    val offset: Offset = Offset.Zero,
    val rotation: Float = 0f,
    val labelOffset: Offset = Offset.Zero,
    val heat: Float = 0f,
) {
    companion object {
        val Rest = ButtonPose()
    }
}

/** One line of the live readout under the stage. */
data class Readout(val label: String, val value: String)

/**
 * Contract every button effect implements. Effects are strategies swapped at runtime by the stage.
 * All coordinates are dp in stage space; the stage scales the canvas for you.
 */
interface ButtonEffect {
    val meta: EffectMeta

    /** Advances the simulation by `frame.dt`. */
    fun update(frame: StageFrame)

    /** Liquid layer only: paint the coverage mask in [ink]. */
    fun DrawScope.drawLiquid(frame: StageFrame, ink: Color) {}

    /** Optional flat drawing above the liquid layer and below the button. */
    fun DrawScope.drawOverlay(frame: StageFrame, colors: OverlayColors) {}

    /** Current label text. Effects that change it back it with Compose state, so only a change recomposes. */
    val label: String get() = meta.buttonLabel

    /** Read every frame in the draw phase; the button's layout box (and so proximity) ignores it. */
    fun pose(): ButtonPose = ButtonPose.Rest

    /** Click, tap or keyboard activation of the button. */
    fun onActivate(frame: StageFrame)

    fun readout(): Readout
}
