package de.nilsdruyen.composeparty.buttons.sticky.effects

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import de.nilsdruyen.composeparty.buttons.sticky.core.SpringBody
import de.nilsdruyen.composeparty.buttons.sticky.core.distanceTo
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageFrame
import kotlin.math.roundToInt

/** Magnet: field lines between two poles and a spring-loaded button that leans toward you. */
class MagnetEffect : ButtonEffect {

    override val meta: EffectMeta = META

    private class Pulse(var radius: Float, var life: Float)

    /** Button offset from its layout position, in dp. */
    private val pull = SpringBody(Offset.Zero)
    private val pulses = mutableListOf<Pulse>()

    override fun update(frame: StageFrame) {
        var target = Offset.Zero
        if (frame.pointer.isActive) {
            val wanted = (frame.pointer.position - frame.center) * (0.32f * frame.proximity)
            val limit = MAX_PULL * frame.motionScale
            val length = wanted.getDistance()
            target = if (length > limit) wanted * (limit / length) else wanted
        }
        pull.springToward(target, STIFFNESS, DAMPING, frame.dt)

        pulses.forEach {
            it.radius += frame.dt * 420f
            it.life -= frame.dt * 1.3f
        }
        pulses.removeAll { it.life <= 0f }
    }

    override fun pose() = ButtonPose(offset = pull.position, labelOffset = pull.position * 0.35f)

    override fun DrawScope.drawOverlay(frame: StageFrame, colors: OverlayColors) {
        val box = frame.box
        val pointer = frame.pointer
        val pole = frame.center + pull.position
        val pointerCharge = if (pointer.isActive) -(0.4f + 1.4f * frame.proximity) else 0f
        val filing = Stroke(width = 1.4f, cap = StrokeCap.Round)

        var y = (frame.size.height / 2f) % GRID
        while (y < frame.size.height) {
            var x = (frame.size.width / 2f) % GRID
            while (x < frame.size.width) {
                val point = Offset(x, y)
                x += GRID
                if (box.distanceTo(point) < 6f) continue
                val field = fieldAt(point, pole, pointer.position, pointerCharge)
                val magnitude = field.getDistance()
                if (magnitude == 0f) continue
                val strength = (magnitude * 60f).coerceIn(0f, 1f)
                val half = field / magnitude * ((3f + strength * 9f) / 2f)
                drawLine(colors.ink, point - half, point + half, filing.width, filing.cap, alpha = 0.1f + strength * 0.6f)
            }
            y += GRID
        }

        val ring = Stroke(width = 2f)
        if (pointer.isActive) {
            drawCircle(colors.accent, 9f + frame.proximity * 6f, pointer.position, alpha = 0.35f + frame.proximity * 0.65f, style = ring)
        }
        pulses.forEach { drawCircle(colors.accent, box.height / 2f + it.radius, pole, alpha = it.life, style = ring) }
    }

    override fun onActivate(frame: StageFrame) {
        pulses += Pulse(radius = 0f, life = 1f)
    }

    override fun readout() = Readout("Pull", "${pull.position.getDistance().roundToInt()}dp")

    /** Superposition of two 2D point charges (field ∝ 1/r). A soft core avoids singularities. */
    private fun fieldAt(point: Offset, pole: Offset, pointer: Offset, pointerCharge: Float): Offset {
        val a = point - pole
        var field = a / (a.x * a.x + a.y * a.y + SOFT_CORE)
        if (pointerCharge != 0f) {
            val b = point - pointer
            field += b * (pointerCharge / (b.x * b.x + b.y * b.y + SOFT_CORE))
        }
        return field
    }

    companion object {
        private const val GRID = 26f
        private const val MAX_PULL = 46f
        private const val STIFFNESS = 180f
        private const val DAMPING = 13f
        private const val SOFT_CORE = 400f

        val META = EffectMeta(
            id = "magnet",
            name = "Magnet",
            layer = EffectLayer.Flat,
            material = null,
            buttonLabel = "Pull me",
            labelColor = Color.Unspecified,
            isSolidButton = true,
            description = "The button is one pole and your pointer is the other. Iron-filing marks trace the combined field " +
                "while the button is pulled toward you on a spring, its label lagging slightly behind. Click to send out a pulse.",
            technique = "2D dipole field sampled on a 26 dp grid · damped spring for the button offset · label parallax · specular glint",
        )
    }
}
