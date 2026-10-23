package de.nilsdruyen.composeparty.buttons.sticky.effects

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.nilsdruyen.composeparty.buttons.sticky.core.LifePulse
import de.nilsdruyen.composeparty.buttons.sticky.core.damp
import de.nilsdruyen.composeparty.buttons.sticky.core.lerp
import de.nilsdruyen.composeparty.buttons.sticky.core.noise1D
import de.nilsdruyen.composeparty.buttons.sticky.core.smoothstep
import de.nilsdruyen.composeparty.buttons.sticky.core.stadiumPerimeter
import de.nilsdruyen.composeparty.buttons.sticky.core.stadiumPerimeterPoint
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageFrame
import de.nilsdruyen.composeparty.buttons.sticky.render.LiquidMaterial
import de.nilsdruyen.composeparty.buttons.sticky.render.fillOrganicStadium
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Ferrofluid: the pointer is a magnet. Spikes rise out of the surface facing it (Rosensweig
 * instability), lean toward it and wobble on springs.
 */
class FerrofluidEffect : ButtonEffect {

    override val meta: EffectMeta = META

    private class Spike(val u: Float, val seed: Float) {
        var height = 0f
        var velocity = 0f
        var direction = Offset(0f, -1f)
    }

    private val spikes = Random(42).let { random -> List(SPIKE_COUNT) { i -> Spike(i.toFloat() / SPIKE_COUNT, random.nextFloat()) } }
    private val spikePath = Path()
    private var lean = 0f
    private var peak = 0f
    private val life = LifePulse()

    override fun update(frame: StageFrame) {
        val dt = frame.dt
        val pointer = frame.pointer.position
        val reach = min(frame.size.width, frame.size.height) * 0.5f
        val substeps = ceil(dt / SUBSTEP).toInt().coerceAtLeast(1)
        val step = dt / substeps
        life.update(dt)
        val sway = life.sway(frame.time)
        var highest = 0f

        for (spike in spikes) {
            val base = frame.box.stadiumPerimeterPoint(spike.u)
            val toPointer = pointer - base.position
            val distance = toPointer.getDistance().takeIf { it > 0f } ?: 1f
            val facing = (base.normal.x * toPointer.x + base.normal.y * toPointer.y) / distance
            val field = if (frame.pointer.isActive) {
                (1f - distance / reach).coerceIn(0f, 1f).pow(1.2f) * smoothstep(-0.3f, 0.6f, facing)
            } else {
                0f
            }

            // At rest the fluid still stirs: small spikes flicker on the side the sway leans toward.
            val restless = max(0f, noise1D(frame.time * 1.6f + spike.seed * 40f) * 0.5f + 0.5f * sway * base.normal.x) * 13f * frame.motionScale
            val target = field * MAX_HEIGHT * (0.65f + 0.35f * spike.seed) + restless * (1f - field)
            repeat(substeps) {
                spike.velocity += ((target - spike.height) * STIFFNESS - spike.velocity * DAMPING) * step
                spike.height += spike.velocity * step
            }

            // Lean toward the magnet, but never fold back into the body.
            val leaning = base.normal + toPointer / distance * (field * 0.9f)
            spike.direction = leaning / (leaning.getDistance().takeIf { it > 0f } ?: 1f)
            highest = max(highest, spike.height)
        }

        peak = highest
        lean = lerp(lean, if (frame.pointer.isActive) frame.proximity else 0f, damp(6f, dt))
    }

    override fun DrawScope.drawLiquid(frame: StageFrame, ink: Color) {
        val box = frame.box
        val aim = frame.aim
        val halfBase = box.stadiumPerimeter / SPIKE_COUNT * 0.78f

        fillOrganicStadium(box, ink) { angle ->
            1f + lean * lean * 10f * max(0f, cos(angle - aim)).pow(3) + life.offsetAt(angle, frame.time, frame.motionScale)
        }

        for (spike in spikes) {
            val height = max(0f, spike.height)
            if (height < 0.8f) continue
            val base = box.stadiumPerimeterPoint(spike.u)
            val direction = spike.direction
            val inset = base.position - base.normal * 3f
            val tangent = Offset(-direction.y, direction.x)
            val width = halfBase * (0.6f + 0.4f * min(1f, height / 14f))
            val tip = inset + direction * (height + 3f)
            val pinch = inset + direction * (height * 0.42f)

            // Concave cusp: both flanks curve inward like a real ferrofluid peak.
            spikePath.reset()
            spikePath.moveTo(inset.x + tangent.x * width, inset.y + tangent.y * width)
            spikePath.quadraticTo(pinch.x + tangent.x * width * 0.12f, pinch.y + tangent.y * width * 0.12f, tip.x, tip.y)
            spikePath.quadraticTo(pinch.x - tangent.x * width * 0.12f, pinch.y - tangent.y * width * 0.12f, inset.x - tangent.x * width, inset.y - tangent.y * width)
            spikePath.close()
            drawPath(spikePath, ink)
        }
    }

    override fun onActivate(frame: StageFrame) {
        spikes.forEach { it.velocity -= 380f + it.seed * 220f }
    }

    override fun readout() = Readout("Spike height", "${max(0f, peak).roundToInt()}dp")

    companion object {
        private const val SPIKE_COUNT = 30
        private const val MAX_HEIGHT = 60f
        private const val STIFFNESS = 300f
        private const val DAMPING = 15f
        private const val SUBSTEP = 1f / 120f

        val META = EffectMeta(
            id = "ferrofluid",
            name = "Ferrofluid",
            layer = EffectLayer.Liquid,
            material = LiquidMaterial.Ferrofluid,
            buttonLabel = "Magnetize",
            labelColor = Color(0xFFF3F3F6),
            description = "Your pointer acts as a magnet. Spikes rise out of the side facing it, grow taller the closer " +
                "you get, lean toward the field and quiver on their own springs. Click to collapse the field and watch it rebound.",
            technique = "Arc-length spaced concave cusps on the outline · per-spike springs · near-black chrome material lit by the pointer",
        )
    }
}
