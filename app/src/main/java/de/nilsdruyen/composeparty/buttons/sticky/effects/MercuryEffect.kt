package de.nilsdruyen.composeparty.buttons.sticky.effects

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.nilsdruyen.composeparty.buttons.sticky.core.LifePulse
import de.nilsdruyen.composeparty.buttons.sticky.core.SpringBody
import de.nilsdruyen.composeparty.buttons.sticky.core.TAU
import de.nilsdruyen.composeparty.buttons.sticky.core.damp
import de.nilsdruyen.composeparty.buttons.sticky.core.distanceTo
import de.nilsdruyen.composeparty.buttons.sticky.core.edgePoint
import de.nilsdruyen.composeparty.buttons.sticky.core.lerp
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageFrame
import de.nilsdruyen.composeparty.buttons.sticky.render.LiquidMaterial
import de.nilsdruyen.composeparty.buttons.sticky.render.fillOrganicStadium
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Mercury: a heavy, silver pool. Come close enough and a drop pinches off and sticks to your
 * pointer; let go and it rolls back and merges. A click shatters the pool into beads that roll
 * home and coalesce whenever they touch.
 */
class MercuryEffect(private val random: Random = Random.Default) : ButtonEffect {

    override val meta: EffectMeta = META

    private class Bead(val body: SpringBody, var radius: Float)

    private var drop: SpringBody? = null
    private var isCaptured = false
    private var isDropInside = true
    private val beads = mutableListOf<Bead>()
    private var wobble = 0f
    private var lean = 0f
    private val life = LifePulse(random)

    override fun update(frame: StageFrame) {
        val dt = frame.dt
        val box = frame.box
        val pointer = frame.pointer
        val drop = drop ?: SpringBody(frame.center).also { drop = it }

        if (!isCaptured && pointer.isActive && frame.proximity > CAPTURE_PROXIMITY) isCaptured = true
        if (isCaptured && !pointer.isActive) isCaptured = false

        val sway = life.sway(frame.time) * 0.22f * box.width * frame.motionScale
        val target = when {
            isCaptured -> pointer.position
            pointer.isActive -> lerp(frame.center, box.edgePoint(frame.aim, DROP_RADIUS * 0.6f), frame.proximity)
            else -> frame.center + Offset(sway, 0f)
        }
        drop.springToward(target, stiffness = if (isCaptured) 110f else 70f, damping = if (isCaptured) 9f else 7f, dt = dt)

        val isInside = box.distanceTo(drop.position) == 0f
        if (isInside && !isDropInside) wobble = min(1.2f, wobble + 0.8f)
        isDropInside = isInside

        lean = lerp(lean, if (pointer.isActive && !isCaptured) frame.proximity else 0f, damp(6f, dt))
        updateBeads(frame)
        wobble *= exp(-dt * 2.5f)
        life.update(dt)
    }

    override fun DrawScope.drawLiquid(frame: StageFrame, ink: Color) {
        val aim = frame.aim
        val time = frame.time
        val motion = frame.motionScale

        fillOrganicStadium(frame.box, ink) { angle ->
            1f + wobble * 5f * motion * sin(7f * angle - time * 16f) +
                lean * lean * 16f * max(0f, cos(angle - aim)).pow(4) +
                life.offsetAt(angle, time, motion)
        }
        drop?.let { drawCircle(ink, DROP_RADIUS, it.position) }
        beads.forEach { drawCircle(ink, it.radius, it.body.position) }
    }

    override fun onActivate(frame: StageFrame) {
        isCaptured = false
        repeat(SHATTER_BEADS) {
            val angle = random.nextFloat() * TAU
            val speed = (220f + random.nextFloat() * 520f) * frame.motionScale
            beads += Bead(
                body = SpringBody(frame.box.edgePoint(angle, -2f), Offset(cos(angle), sin(angle)) * speed),
                radius = 2f + random.nextFloat() * 5.5f,
            )
        }
        wobble = 1.2f
    }

    override fun readout() = Readout("Loose drops", (beads.size + if (isDropInside) 0 else 1).toString())

    private fun updateBeads(frame: StageFrame) {
        val size = frame.size
        for (bead in beads) {
            val body = bead.body
            body.springToward(frame.center, stiffness = 2.4f, damping = 1.5f, dt = frame.dt)
            // Bounce off the stage walls so beads stay in view.
            val r = bead.radius
            var (x, y) = body.position
            var (vx, vy) = body.velocity
            if (x < r || x > size.width - r) {
                vx *= -0.5f
                x = x.coerceIn(r, size.width - r)
            }
            if (y < r || y > size.height - r) {
                vy *= -0.5f
                y = y.coerceIn(r, size.height - r)
            }
            body.position = Offset(x, y)
            body.velocity = Offset(vx, vy)
        }

        // Coalescence: touching beads merge, conserving area and momentum.
        var i = 0
        while (i < beads.size) {
            var j = beads.lastIndex
            while (j > i) {
                val a = beads[i]
                val b = beads[j]
                if ((a.body.position - b.body.position).getDistance() <= (a.radius + b.radius) * 0.8f) {
                    val areaA = a.radius * a.radius
                    val areaB = b.radius * b.radius
                    val total = areaA + areaB
                    a.body.position = (a.body.position * areaA + b.body.position * areaB) / total
                    a.body.velocity = (a.body.velocity * areaA + b.body.velocity * areaB) / total
                    a.radius = sqrt(total)
                    beads.removeAt(j)
                }
                j--
            }
            i++
        }

        val before = beads.size
        beads.removeAll { frame.box.distanceTo(it.body.position) == 0f }
        if (beads.size < before) wobble = min(1.2f, wobble + 0.15f * (before - beads.size))
    }

    companion object {
        private const val CAPTURE_PROXIMITY = 0.8f
        private const val SHATTER_BEADS = 28
        private const val DROP_RADIUS = 15f

        val META = EffectMeta(
            id = "mercury",
            name = "Mercury",
            layer = EffectLayer.Liquid,
            material = LiquidMaterial.Mercury,
            buttonLabel = "Pull a drop",
            labelColor = Color(0xFF121318),
            description = "A heavy pool of liquid metal. Get very close and a drop pinches off and follows your pointer " +
                "around the stage; let go and it rolls back and merges with a ripple. Click to shatter the pool into beads " +
                "that roll home and fuse whenever they touch.",
            technique = "Spring-driven capture with low damping for weight · bead coalescence that conserves area · " +
                "metaball necks from the blur threshold · chrome material",
        )
    }
}
