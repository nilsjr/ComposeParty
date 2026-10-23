package de.nilsdruyen.composeparty.buttons.sticky.effects

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.nilsdruyen.composeparty.buttons.sticky.core.SpringBody
import de.nilsdruyen.composeparty.buttons.sticky.core.damp
import de.nilsdruyen.composeparty.buttons.sticky.core.lerp
import de.nilsdruyen.composeparty.buttons.sticky.core.smoothstep
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageFrame
import de.nilsdruyen.composeparty.buttons.sticky.render.LiquidMaterial
import de.nilsdruyen.composeparty.buttons.sticky.render.fillOrganicStadium
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Glass: a clear button that refracts the stage behind it. A lens of glass flows out toward the
 * pointer, so the magnified dot grid travels with you; a click sends a ripple through the outline.
 */
class GlassEffect : ButtonEffect {

    override val meta: EffectMeta = META

    private var lens: SpringBody? = null
    private var lensRadius = 0f
    private var bulge = 0f
    private var ripple = 0f

    override fun update(frame: StageFrame) {
        val body = lens ?: SpringBody(frame.center).also { lens = it }
        val pull = if (frame.pointer.isActive) smoothstep(0.25f, 0.95f, frame.proximity) else 0f
        body.springToward(lerp(frame.center, frame.pointer.position, pull * 0.92f), stiffness = 120f, damping = 12f, dt = frame.dt)

        val radiusTarget = if (frame.pointer.isActive) 8f + frame.proximity * 20f else 0f
        lensRadius = lerp(lensRadius, radiusTarget, damp(8f, frame.dt))
        bulge = lerp(bulge, if (frame.pointer.isActive) frame.proximity else 0f, damp(6f, frame.dt))
        ripple *= exp(-frame.dt * 2.2f)
    }

    override fun DrawScope.drawLiquid(frame: StageFrame, ink: Color) {
        val aim = frame.aim
        fillOrganicStadium(frame.box, ink) { angle ->
            1f + ripple * 4f * frame.motionScale * sin(8f * angle - frame.time * 18f) +
                bulge * bulge * 12f * max(0f, cos(angle - aim)).pow(4)
        }
        lens?.let { if (lensRadius > 0.5f) drawCircle(ink, lensRadius, it.position) }
    }

    override fun onActivate(frame: StageFrame) {
        ripple = 1f
    }

    override fun readout() = Readout("Lens", "${(lensRadius * 2f).roundToInt()}dp")

    companion object {
        val META = EffectMeta(
            id = "glass",
            name = "Glass",
            layer = EffectLayer.Liquid,
            material = LiquidMaterial.Glass,
            buttonLabel = "Look through",
            labelColor = Color(0xFF111218),
            description = "A button cast from clear glass. It bends the dot grid behind it with a faint rainbow " +
                "fringe at the edges, and a lens flows out toward your pointer. Click to send a ripple through it.",
            technique = "Backdrop rebuilt in the shader and refracted through the height-field normals · dispersion · Fresnel reflections",
        )
    }
}
