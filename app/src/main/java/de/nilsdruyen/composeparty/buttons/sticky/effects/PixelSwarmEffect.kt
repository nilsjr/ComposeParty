package de.nilsdruyen.composeparty.buttons.sticky.effects

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.nilsdruyen.composeparty.buttons.sticky.core.SpringBody
import de.nilsdruyen.composeparty.buttons.sticky.core.TAU
import de.nilsdruyen.composeparty.buttons.sticky.core.angleTo
import de.nilsdruyen.composeparty.buttons.sticky.core.noise1D
import de.nilsdruyen.composeparty.buttons.sticky.core.stadiumContains
import de.nilsdruyen.composeparty.buttons.sticky.engine.StageFrame
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.random.Random

/**
 * Pixel swarm: an 8-bit button drawn cell by cell. As the pointer approaches, pixels on the near
 * side break loose and stream into a swarm orbiting the cursor; when it leaves they fly home and
 * snap back into their slots.
 */
class PixelSwarmEffect(private val random: Random = Random.Default) : ButtonEffect {

    override val meta: EffectMeta = META

    private class Pixel(
        val home: Offset,
        val color: Color,
        val seed: Float,
        val angle: Float,
        var orbit: Float,
        val orbitRadius: Float,
        val orbitSpeed: Float,
    ) {
        val body = SpringBody(home)
        var isOut = false
        var isHome = true
        var scatter = 0f
    }

    private var pixels: List<Pixel> = emptyList()
    private var layoutBox: Rect? = null

    override fun update(frame: StageFrame) {
        ensureLayout(frame.box)
        val aim = frame.aim
        for (pixel in pixels) {
            val facing = 0.5f + 0.5f * cos(pixel.angle - aim)
            val threshold = 0.35f + pixel.seed * 0.45f + (1f - facing) * 0.35f
            pixel.isOut = frame.pointer.isActive && frame.proximity > threshold
            pixel.scatter = (pixel.scatter - frame.dt).coerceAtLeast(0f)
            if (pixel.isOut || !pixel.isHome) steer(pixel, frame)
        }
    }

    override fun DrawScope.drawOverlay(frame: StageFrame) {
        val cellSize = Size(CELL, CELL)
        val half = Offset(CELL / 2f, CELL / 2f)
        val loose = pixels.filterNot { it.isHome }

        pixels.filter { it.isHome }.forEach { drawRect(it.color, topLeft = it.home - half, size = cellSize) }
        // Empty slots stay faintly visible so the drained shape still reads as a button.
        loose.forEach {
            drawRect(Palette.outline.copy(alpha = 0.18f), topLeft = it.home - half + Offset(1f, 1f), size = Size(CELL - 2f, CELL - 2f))
        }
        loose.forEach {
            val snapped = Offset((it.body.position.x - CELL / 2f).roundToInt().toFloat(), (it.body.position.y - CELL / 2f).roundToInt().toFloat())
            drawRect(it.color, topLeft = snapped, size = cellSize)
        }
    }

    override fun onActivate(frame: StageFrame) {
        pixels.filterNot { it.isHome }.forEach { pixel ->
            val angle = random.nextFloat() * TAU
            val speed = (380f + random.nextFloat() * 380f) * frame.motionScale
            pixel.body.velocity = Offset(cos(angle), sin(angle)) * speed
            pixel.scatter = SCATTER_SECONDS
        }
    }

    override fun readout() = Readout("Pixels out", "${pixels.count { !it.isHome }}/${pixels.size}")

    /** Orbit the pointer while drained, otherwise fly home and snap into the slot. */
    private fun steer(pixel: Pixel, frame: StageFrame) {
        val body = pixel.body
        pixel.isHome = false
        when {
            pixel.scatter > 0f -> {
                body.velocity *= exp(-frame.dt * 3f)
                body.position += body.velocity * frame.dt
            }
            pixel.isOut -> {
                pixel.orbit += frame.dt * pixel.orbitSpeed * frame.motionScale
                val radius = pixel.orbitRadius * (1f + 0.25f * noise1D(frame.time * 1.3f + pixel.seed * 50f))
                val target = frame.pointer.position + Offset(cos(pixel.orbit), sin(pixel.orbit)) * radius
                body.springToward(target, stiffness = 55f, damping = 7f, dt = frame.dt)
            }
            else -> {
                body.springToward(pixel.home, stiffness = 90f, damping = 12f, dt = frame.dt)
                val isSettled = (body.position - pixel.home).getDistance() < 0.8f && body.velocity.getDistance() < 30f
                if (isSettled) {
                    body.position = pixel.home
                    body.velocity = Offset.Zero
                    pixel.isHome = true
                }
            }
        }
    }

    /** Rasterises the pill into cells with a five-step colour ramp; reruns only when the button moves or resizes. */
    private fun ensureLayout(box: Rect) {
        if (box == layoutBox) return
        layoutBox = box

        val cols = (box.width / CELL).roundToInt()
        val rows = (box.height / CELL).roundToInt()
        val origin = Offset(box.center.x - cols * CELL / 2f, box.center.y - rows * CELL / 2f)
        fun cellCenter(col: Int, row: Int) = origin + Offset((col + 0.5f) * CELL, (row + 0.5f) * CELL)
        fun isInside(col: Int, row: Int) =
            col in 0 until cols && row in 0 until rows && box.stadiumContains(cellCenter(col, row), CELL * 0.2f)

        val layoutRandom = Random(99)
        val result = mutableListOf<Pixel>()
        for (row in 0 until rows) {
            var interiorIndex = 0
            for (col in 0 until cols) {
                if (!isInside(col, row)) continue
                val isEdge = !isInside(col - 1, row) || !isInside(col + 1, row) || !isInside(col, row - 1) || !isInside(col, row + 1)
                val color = when {
                    isEdge -> Palette.outline
                    !isInside(col, row - 2) -> if (interiorIndex++ < 3) Palette.shine else Palette.highlight
                    !isInside(col, row + 2) -> Palette.shade
                    else -> Palette.body
                }
                val home = cellCenter(col, row)
                result += Pixel(
                    home = home,
                    color = color,
                    seed = layoutRandom.nextFloat(),
                    angle = box.angleTo(home),
                    orbit = layoutRandom.nextFloat() * TAU,
                    orbitRadius = 12f + layoutRandom.nextFloat() * 34f,
                    orbitSpeed = (1.6f + layoutRandom.nextFloat() * 2.4f) * if (layoutRandom.nextFloat() < 0.85f) 1f else -1f,
                )
            }
        }
        pixels = result
    }

    private object Palette {
        val outline = Color(0xFF1A1C2C)
        val shade = Color(0xFF29366F)
        val body = Color(0xFF3B5DC9)
        val highlight = Color(0xFF41A6F6)
        val shine = Color(0xFF73EFF7)
    }

    companion object {
        private const val CELL = 6f
        private const val SCATTER_SECONDS = 0.45f

        val META = EffectMeta(
            id = "pixel",
            name = "Pixel",
            layer = EffectLayer.Flat,
            material = null,
            buttonLabel = "Insert coin",
            labelColor = Color(0xFFF4F4F4),
            labelStyle = LabelStyle.Pixel,
            description = "An 8-bit button built from single pixels. Come closer and pixels break loose and swarm " +
                "around your cursor, leaving empty slots behind. Pull away and they fly home. Click to scatter them.",
            technique = "Button rasterised into a 6 dp grid with a five-colour ramp · per-pixel release thresholds · orbiting spring swarm",
        )
    }
}
