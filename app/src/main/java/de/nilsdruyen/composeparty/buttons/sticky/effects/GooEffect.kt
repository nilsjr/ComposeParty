package de.nilsdruyen.composeparty.buttons.sticky.effects

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.nilsdruyen.composeparty.buttons.sticky.core.SpringBody
import de.nilsdruyen.composeparty.buttons.sticky.core.TAU
import de.nilsdruyen.composeparty.buttons.sticky.core.damp
import de.nilsdruyen.composeparty.buttons.sticky.core.easeInOutCubic
import de.nilsdruyen.composeparty.buttons.sticky.core.edgePoint
import de.nilsdruyen.composeparty.buttons.sticky.core.lerp
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageFrame
import de.nilsdruyen.composeparty.buttons.sticky.render.LiquidMaterial
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/** Goo: a jelly button whose droplets stretch toward the pointer on underdamped springs. */
class GooEffect(private val random: Random = Random.Default) : ButtonEffect {

    override val meta: EffectMeta = META

    private class Drop(val angle: Float, val pull: Float, val radius: Float) {
        var body: SpringBody? = null
    }

    private val drops = Random(21).let { layout ->
        List(DROP_COUNT) { i ->
            Drop(
                angle = i.toFloat() / DROP_COUNT * TAU + (layout.nextFloat() - 0.5f) * 0.4f,
                pull = layout.nextFloat(),
                radius = 6f + layout.nextFloat() * 8f,
            )
        }
    }
    private var blobRadius = 0f
    private var bulge = 0f
    private var detached = 0

    override fun update(frame: StageFrame) {
        val pointer = frame.pointer
        val aim = frame.aim
        var out = 0

        for (drop in drops) {
            val home = frame.box.edgePoint(drop.angle + sin(frame.time * 0.8f + drop.pull * 9f) * 0.08f, drop.radius * 0.9f)
            val body = drop.body ?: SpringBody(home).also { drop.body = it }

            // Drops facing the pointer let go first; `pull` adds jitter so they never leave in lockstep.
            val facing = 0.5f + 0.5f * cos(drop.angle - aim)
            val threshold = 1f - (0.35f + 0.65f * (facing * 0.7f + drop.pull * 0.3f))
            val release = if (pointer.isActive) ((frame.proximity - threshold * 0.85f) / 0.35f).coerceIn(0f, 1f) else 0f
            val target = lerp(home, pointer.position, easeInOutCubic(release) * lerp(0.55f, 0.95f, drop.pull))

            body.springToward(target, STIFFNESS, DAMPING, frame.dt)
            if ((body.position - home).getDistance() > 18f) out++
        }

        detached = out
        bulge = lerp(bulge, if (pointer.isActive) frame.proximity else 0f, damp(8f, frame.dt))
        val blobTarget = if (pointer.isActive && frame.proximity > 0.02f) 6f + frame.proximity * 18f else 0f
        blobRadius = lerp(blobRadius, blobTarget, damp(9f, frame.dt))
    }

    override fun DrawScope.drawLiquid(frame: StageFrame, ink: Color) {
        val box = frame.box
        drawRoundRect(ink, topLeft = box.topLeft, size = box.size, cornerRadius = CornerRadius(box.height / 2f))

        if (bulge > 0.01f) {
            val aim = frame.aim
            val radius = box.height * 0.38f * bulge
            val push = bulge * 14f - radius * 0.4f
            drawCircle(ink, radius, box.edgePoint(aim) + Offset(cos(aim), sin(aim)) * push)
        }

        drops.forEach { drop -> drop.body?.let { drawCircle(ink, drop.radius, it.position) } }
        if (blobRadius > 0.5f) drawCircle(ink, blobRadius, frame.pointer.position)
    }

    override fun onActivate(frame: StageFrame) {
        for (drop in drops) {
            val body = drop.body ?: continue
            val angle = atan2(body.position.y - frame.center.y, body.position.x - frame.center.x)
            val speed = (520f + random.nextFloat() * 380f) * frame.motionScale
            body.velocity += Offset(cos(angle), sin(angle)) * speed
        }
        blobRadius *= 0.3f
    }

    override fun readout() = Readout("Drops out", "$detached/$DROP_COUNT")

    companion object {
        private const val DROP_COUNT = 11
        private const val STIFFNESS = 140f
        private const val DAMPING = 11f

        val META = EffectMeta(
            id = "goo",
            name = "Goo",
            layer = EffectLayer.Liquid,
            material = LiquidMaterial.Jelly,
            buttonLabel = "Squish me",
            labelColor = Color.White,
            description = "The button is a blob of jelly. Droplets leave it in a staggered order and stretch toward your " +
                "pointer, fusing into one body as you get close. Thick parts glow from within. Click to splash it apart.",
            technique = "Underdamped springs per droplet · metaball fusion from the blur threshold · subsurface glow from the height field",
        )
    }
}
