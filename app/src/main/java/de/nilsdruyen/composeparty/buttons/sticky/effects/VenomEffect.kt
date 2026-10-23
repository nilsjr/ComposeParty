package de.nilsdruyen.composeparty.buttons.sticky.effects

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import de.nilsdruyen.composeparty.buttons.sticky.core.LifePulse
import de.nilsdruyen.composeparty.buttons.sticky.core.TAU
import de.nilsdruyen.composeparty.buttons.sticky.core.damp
import de.nilsdruyen.composeparty.buttons.sticky.core.easeOutCubic
import de.nilsdruyen.composeparty.buttons.sticky.core.edgePoint
import de.nilsdruyen.composeparty.buttons.sticky.core.lerp
import de.nilsdruyen.composeparty.buttons.sticky.core.smoothstep
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageFrame
import de.nilsdruyen.composeparty.buttons.sticky.render.LiquidMaterial
import de.nilsdruyen.composeparty.buttons.sticky.render.fillOrganicStadium
import de.nilsdruyen.composeparty.buttons.sticky.render.fillRibbon
import de.nilsdruyen.composeparty.buttons.sticky.render.sampleSpline
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

/**
 * Venom: glue strands simulated as Verlet ropes.
 * - Strands bond when the pointer comes close and stay bonded while it moves away.
 * - Rest length creeps toward the current length (viscous flow) and tightens again (surface
 *   tension), so slow pulls stretch smoothly while fast moves pull taut and slack strands droop.
 * - Width follows volume conservation: a longer strand is a thinner strand.
 * - Each strand has its own breaking length; it snaps, recoils into the body and sheds a drop.
 */
class VenomEffect(private val random: Random = Random.Default) : ButtonEffect {

    override val meta: EffectMeta = META

    private enum class StrandState { Reaching, Attached, Snapped }

    private class RopePoint(var x: Float, var y: Float) {
        var previousX = x
        var previousY = y

        /** Moves the point to [target] but keeps its last position, so a later release inherits velocity. */
        fun pin(target: Offset) {
            previousX = x
            previousY = y
            x = target.x
            y = target.y
        }

        val position: Offset get() = Offset(x, y)
    }

    private class Strand(
        val anchorAngle: Float,
        val tipAngle: Float,
        val volume: Float,
        val maxLength: Float,
        anchor: Offset,
    ) {
        var state = StrandState.Reaching
        var reach = 0f
        var rest = 0.5f
        var age = 0f
        var length = 0f
        val points = List(SEGMENTS + 1) { RopePoint(anchor.x, anchor.y) }
    }

    private class Droplet(var position: Offset, var velocity: Offset, val radius: Float)

    private class Drip(val x: Float, val maxLength: Float, val speed: Float, val phase: Float) {
        var length = 0f
    }

    private val strands = mutableListOf<Strand>()
    private val droplets = mutableListOf<Droplet>()
    private val drips = mutableListOf<Drip>()
    private var blobRadius = 0f
    private var spawnCooldown = 0f
    private var dripTimer = 1.5f
    private var shiver = 0f
    private val life = LifePulse(random)

    override fun update(frame: StageFrame) {
        val dt = frame.dt
        if (!frame.pointer.isActive) strands.filter { it.state != StrandState.Snapped }.forEach { snap(it, dt) }

        val bonded = strands.count { it.state != StrandState.Snapped }
        val wanted = if (frame.pointer.isActive) (smoothstep(0.42f, 0.95f, frame.proximity) * MAX_BONDED).roundToInt() else 0
        spawnCooldown -= dt
        if (bonded < wanted && spawnCooldown <= 0f) {
            strands += createStrand(frame.box, frame.aim, frame.proximity)
            spawnCooldown = 0.045f
        }

        strands.forEach { simulate(it, frame) }
        strands.removeAll { it.state == StrandState.Snapped && (it.length < 6f || it.age > 3f) }
        while (strands.size > MAX_STRANDS) strands.removeAt(0)

        val attached = strands.count { it.state == StrandState.Attached }
        val blobTarget = if (attached > 0) 3f + sqrt(attached.toFloat()) * 3.2f else 0f
        blobRadius = lerp(blobRadius, blobTarget, damp(8f, dt))

        shiver *= exp(-dt * 4f)
        life.update(dt)
        updateDrips(frame)
        updateDroplets(dt, frame.size.height)
    }

    override fun DrawScope.drawLiquid(frame: StageFrame, ink: Color) {
        val motion = frame.motionScale
        val aim = frame.aim
        val time = frame.time
        val breathe = 1.4f + sin(time * 2.1f) * 0.8f * motion

        fillOrganicStadium(frame.box, ink) { angle ->
            val ripple = 0.5f * sin(5f * angle + time * 1.3f) +
                0.3f * sin(9f * angle - time * 0.9f + 1.7f) +
                0.2f * sin(14f * angle + time * 2.3f)
            val toward = max(0f, cos(angle - aim)).pow(6)
            val shake = shiver * 5f * sin(11f * angle + time * 30f)
            breathe + frame.proximity * (4f + ripple * 6f * motion) + frame.proximity * frame.proximity * 22f * toward + shake +
                life.offsetAt(angle, time, motion)
        }

        drips.forEach { drawDrip(it, frame.box, time, ink) }
        strands.forEach { drawStrand(it, ink) }
        if (blobRadius > 0.5f) drawCircle(ink, blobRadius, frame.pointer.position)

        droplets.forEach { drop ->
            val speed = drop.velocity.getDistance()
            val stretch = 1f + speed / 1100f
            val angle = atan2(drop.velocity.y, drop.velocity.x) * 180f / kotlin.math.PI.toFloat()
            rotate(angle, pivot = drop.position) {
                drawOval(
                    ink,
                    topLeft = Offset(drop.position.x - drop.radius * stretch, drop.position.y - drop.radius),
                    size = androidx.compose.ui.geometry.Size(drop.radius * stretch * 2f, drop.radius * 2f),
                )
            }
        }
    }

    override fun onActivate(frame: StageFrame) {
        strands.filter { it.state != StrandState.Snapped }.forEach { snap(it, frame.dt.coerceAtLeast(1f / 60f)) }
        shiver = 1f
        spawnCooldown = 0.7f
    }

    override fun readout() = Readout("Strands bonded", strands.count { it.state == StrandState.Attached }.toString())

    private fun createStrand(box: Rect, aim: Float, proximity: Float): Strand {
        val seed = random.nextFloat()
        val anchorAngle = aim + (random.nextFloat() - 0.5f) * lerp(2.2f, 1.3f, proximity)
        val volume = (500f + seed * 1300f) * lerp(0.7f, 1.2f, proximity)
        return Strand(
            anchorAngle = anchorAngle,
            tipAngle = random.nextFloat() * TAU,
            volume = volume,
            maxLength = 110f + seed * 250f + volume * 0.04f,
            anchor = box.edgePoint(anchorAngle, 2f),
        )
    }

    private fun simulate(strand: Strand, frame: StageFrame) {
        val dt = frame.dt
        val points = strand.points
        val last = points.lastIndex
        val isFreeEnd = strand.state == StrandState.Snapped
        strand.age += dt

        val gravityStep = GRAVITY * dt * dt
        for (i in 1..last) {
            if (i == last && !isFreeEnd) continue
            val p = points[i]
            val vx = (p.x - p.previousX) * AIR_DRAG
            val vy = (p.y - p.previousY) * AIR_DRAG
            p.previousX = p.x
            p.previousY = p.y
            p.x += vx
            p.y += vy + gravityStep
        }

        val anchor = frame.box.edgePoint(strand.anchorAngle, 2f)
        val tip = frame.pointer.position + Offset(cos(strand.tipAngle), sin(strand.tipAngle)) * (blobRadius * 0.5f)
        points[0].pin(anchor)
        when (strand.state) {
            StrandState.Reaching -> {
                strand.reach = minOf(1f, strand.reach + dt * 5.5f)
                points[last].pin(lerp(anchor, tip, easeOutCubic(strand.reach)))
                if (strand.reach >= 1f) strand.state = StrandState.Attached
            }
            StrandState.Attached -> points[last].pin(tip)
            StrandState.Snapped -> Unit
        }

        if (isFreeEnd) {
            strand.rest *= exp(-dt * 7f) // surface tension pulls the broken strand back into the body
        } else {
            val chord = (points[last].position - anchor).getDistance()
            val target = chord / last * 1.04f
            val rate = if (target > strand.rest) 3f else 6f // stretch slowly (viscous), tighten faster
            strand.rest = lerp(strand.rest, target, damp(rate, dt))
        }

        solveRope(strand, isFreeEnd)

        strand.length = (0 until last).sumOf { i ->
            hypot(points[i + 1].x - points[i].x, points[i + 1].y - points[i].y).toDouble()
        }.toFloat()

        if (strand.state == StrandState.Attached && strand.length > strand.maxLength) snap(strand, dt)
    }

    /** Rope constraint: segments resist stretching beyond rest length but may go slack and sag. */
    private fun solveRope(strand: Strand, isFreeEnd: Boolean) {
        val points = strand.points
        val last = points.lastIndex
        repeat(ITERATIONS) {
            for (i in 0 until last) {
                val a = points[i]
                val b = points[i + 1]
                val dx = b.x - a.x
                val dy = b.y - a.y
                val distance = hypot(dx, dy)
                if (distance <= strand.rest || distance == 0f) continue

                val weightA = if (i == 0) 0f else 1f
                val weightB = if (i + 1 == last && !isFreeEnd) 0f else 1f
                val total = weightA + weightB
                if (total == 0f) continue

                val correction = (distance - strand.rest) / distance * 0.8f
                a.x += dx * correction * (weightA / total)
                a.y += dy * correction * (weightA / total)
                b.x -= dx * correction * (weightB / total)
                b.y -= dy * correction * (weightB / total)
            }
        }
    }

    private fun snap(strand: Strand, dt: Float) {
        strand.state = StrandState.Snapped
        val end = strand.points.last()
        val step = max(dt, 1f / 120f)
        droplets += Droplet(
            position = end.position,
            velocity = Offset((end.x - end.previousX) / step, (end.y - end.previousY) / step),
            radius = 1.6f + sqrt(strand.volume) * 0.05f,
        )
    }

    private fun DrawScope.drawStrand(strand: Strand, ink: Color) {
        val path = sampleSpline(strand.points.map { it.position })
        val isBonded = strand.state != StrandState.Snapped
        val thickness = sqrt(strand.volume / max(strand.length, 10f)).coerceIn(0.7f, 8f)
        val tipGrowth = if (strand.state == StrandState.Reaching) strand.reach else 1f

        fillRibbon(path, ink) { s ->
            val neck = thickness * (0.38f + 0.62f * abs(2f * s - 1f).pow(2.2f))
            val baseMeniscus = thickness * 1.8f * exp(-s * 9f)
            val tipMeniscus = if (isBonded) {
                thickness * 1.2f * exp(-(1f - s) * 9f) * tipGrowth
            } else {
                thickness * 2.2f * exp(-(1f - s) * 5f) // a recoiling glob at the broken end
            }
            neck + baseMeniscus + tipMeniscus
        }
    }

    /** Slow drips that grow from the underside of the skin, neck down and fall. */
    private fun updateDrips(frame: StageFrame) {
        val box = frame.box
        dripTimer -= frame.dt * (0.3f + frame.proximity * 2.2f)
        if (dripTimer <= 0f) {
            dripTimer = 1.4f + random.nextFloat() * 2.2f
            drips += Drip(
                x = box.left + box.height / 2f + random.nextFloat() * (box.width - box.height),
                maxLength = 22f + random.nextFloat() * 18f,
                speed = 12f + random.nextFloat() * 10f,
                phase = random.nextFloat() * TAU,
            )
        }
        drips.forEach { it.length += it.speed * frame.dt * (1f + it.length / 30f) }
        drips.filter { it.length >= it.maxLength }.forEach { drip ->
            droplets += Droplet(
                position = Offset(drip.x, box.bottom + drip.length),
                velocity = Offset(0f, drip.speed * 3f),
                radius = 3f + drip.length * 0.1f,
            )
        }
        drips.removeAll { it.length >= it.maxLength }
    }

    private fun DrawScope.drawDrip(drip: Drip, box: Rect, time: Float, ink: Color) {
        val top = box.bottom - 3f
        val sway = sin(time * 2.4f + drip.phase) * 1.5f
        val path = List(8) { i ->
            val s = i / 7f
            Offset(drip.x + sway * s * s, top + drip.length * s)
        }
        fillRibbon(path, ink) { s -> lerp(9f, 1.8f, s.pow(0.6f)) }
        drawCircle(ink, 2.5f + drip.length * 0.1f, path.last())
    }

    private fun updateDroplets(dt: Float, height: Float) {
        droplets.forEach { drop ->
            drop.velocity = Offset(drop.velocity.x * exp(-dt * 0.8f), drop.velocity.y + GRAVITY * dt)
            drop.position += drop.velocity * dt
        }
        droplets.removeAll { it.position.y > height + 30f }
    }

    companion object {
        private const val MAX_BONDED = 11
        private const val MAX_STRANDS = 26
        private const val SEGMENTS = 14
        private const val ITERATIONS = 8
        private const val GRAVITY = 900f
        private const val AIR_DRAG = 0.982f

        val META = EffectMeta(
            id = "venom",
            name = "Venom",
            layer = EffectLayer.Liquid,
            material = LiquidMaterial.Symbiote,
            buttonLabel = "Get closer",
            labelColor = Color(0xFFF3F3F6),
            description = "Touch the skin and glue strands bond to your pointer. Pull away and they stretch, " +
                "thin out, sag and snap one by one, whipping back into the body and dripping. Click to tear them all.",
            technique = "Verlet rope physics with viscous rest-length creep · volume-conserving width · single-pass liquid shader",
        )
    }
}
