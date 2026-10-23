package de.nilsdruyen.composeparty.buttons.sticky.effects

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import de.nilsdruyen.composeparty.buttons.sticky.core.lerp
import de.nilsdruyen.composeparty.buttons.sticky.core.noise1D
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageFrame
import kotlin.math.max
import kotlin.random.Random

/** Nervous: a regular button that trembles, heats up and sweats as you approach. */
class NervousEffect(private val random: Random = Random.Default) : ButtonEffect {

    override val meta: EffectMeta = META

    private class Sweat(var position: Offset, var velocity: Offset)

    private var phase = 0f
    private var calm = 0f
    private var heat = 0f
    private var amplitude = 0f
    private val sweat = mutableListOf<Sweat>()

    override var label: String by mutableStateOf(META.buttonLabel)
        private set

    override fun update(frame: StageFrame) {
        val dt = frame.dt
        calm = max(0f, calm - dt)
        heat = if (calm > 0f) 0f else frame.proximity
        amplitude = heat * heat * MAX_AMPLITUDE * frame.motionScale
        phase += dt * lerp(18f, 55f, heat)
        label = labelForHeat()

        if (heat > 0.75f && random.nextFloat() < dt * 7f) spawnSweat(frame.box)
        sweat.forEach { drop ->
            drop.velocity += Offset(0f, GRAVITY * dt)
            drop.position += drop.velocity * dt
        }
        sweat.removeAll { it.position.y > frame.size.height + 20f }
    }

    override fun pose() = ButtonPose(
        offset = Offset(amplitude * noise1D(phase), amplitude * 0.6f * noise1D(phase + 31.7f)),
        rotation = amplitude * 0.5f * noise1D(phase + 77.1f),
        heat = heat * 0.85f,
    )

    override fun DrawScope.drawOverlay(frame: StageFrame, colors: OverlayColors) {
        drawTrembleMarks(frame.box, colors.ink)
        sweat.forEach { drawOval(colors.accent, topLeft = it.position - Offset(2.4f, 3.6f), size = Size(4.8f, 7.2f)) }
    }

    override fun onActivate(frame: StageFrame) {
        calm = CALM_SECONDS
    }

    override fun readout() = Readout("Amplitude", "${"%.1f".format(amplitude)}dp")

    private fun labelForHeat() = when {
        calm > 0f -> "…fine."
        heat < 0.35f -> META.buttonLabel
        heat < 0.75f -> "Please, no"
        else -> "Stay back!"
    }

    private fun spawnSweat(box: Rect) {
        val side = if (random.nextBoolean()) -1f else 1f
        val x = box.center.x + side * (box.width / 2f - box.height * 0.35f)
        sweat += Sweat(Offset(x, box.top + 4f), Offset(side * (30f + random.nextFloat() * 50f), -120f - random.nextFloat() * 80f))
    }

    /** Cartoon tremble marks: short arcs hugging both pill caps, jittering with the button. */
    private fun DrawScope.drawTrembleMarks(box: Rect, ink: Color) {
        if (heat < 0.05f) return
        val capOffset = box.width / 2f - box.height / 2f
        val stroke = Stroke(width = 2f, cap = StrokeCap.Round)
        for (side in intArrayOf(-1, 1)) {
            val cap = Offset(box.center.x + side * capOffset, box.center.y)
            val baseAngle = if (side > 0) 0f else 180f
            repeat(3) { i ->
                val jitter = noise1D(phase * 1.7f + i * 9f + side * 5f) * amplitude * 0.6f
                val radius = box.height / 2f + 10f + i * 8f + jitter
                val span = Math.toDegrees(0.55 - i * 0.1).toFloat()
                drawArc(
                    color = ink,
                    startAngle = baseAngle - span,
                    sweepAngle = span * 2f,
                    useCenter = false,
                    topLeft = cap - Offset(radius, radius),
                    size = Size(radius * 2f, radius * 2f),
                    alpha = heat,
                    style = stroke,
                )
            }
        }
    }

    companion object {
        private const val MAX_AMPLITUDE = 9f
        private const val CALM_SECONDS = 1.4f
        private const val GRAVITY = 900f

        val META = EffectMeta(
            id = "nervous",
            name = "Nervous",
            layer = EffectLayer.Flat,
            material = null,
            buttonLabel = "Don't click",
            labelColor = Color.Unspecified,
            isSolidButton = true,
            description = "The button gets more anxious the closer you come. It trembles, heats up and starts sweating, " +
                "and its glossy coat catches the light. Click it anyway and it gives up for a moment.",
            technique = "Value-noise jitter on the layer transform · amplitude scales with proximity² · Oklab colour lerp on heat · specular glint",
        )
    }
}
