package de.nilsdruyen.composeparty.buttons.sticky.engine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import de.nilsdruyen.composeparty.buttons.sticky.core.damp
import de.nilsdruyen.composeparty.buttons.sticky.core.distanceTo
import de.nilsdruyen.composeparty.buttons.sticky.core.lerp
import de.nilsdruyen.composeparty.buttons.sticky.core.smoothstep
import de.nilsdruyen.composeparty.buttons.sticky.effects.ButtonEffect
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * Platform-independent heart of the stage: collects pointer input, smooths it, computes proximity,
 * advances the active effect and the light. The UI layer only forwards input and draws.
 */
class StageController(
    val lightRig: LightRig,
    initialEffect: ButtonEffect,
    private val reducedMotion: Boolean = false,
) {
    /** Observable so the stage recomposes (layer, label) when the effect changes. */
    var effect: ButtonEffect by mutableStateOf(initialEffect)
        private set

    /** Last computed frame, or null before the first step. */
    var frame: StageFrame? = null
        private set

    var light: LightState? = null
        private set

    /** Becomes true on the first real pointer interaction; until then a demo pointer runs. */
    var isEngaged: Boolean = false
        private set

    private var rawPosition = Offset.Zero
    private var isPointerActive = false
    private var snapPending = false
    private var smoothed = Offset.Zero
    private var proximity = 0f
    private var time = 0f

    fun selectEffect(next: ButtonEffect) {
        effect = next
    }

    /** Pointer moved, entered or pressed at [position] (dp, stage coordinates). */
    fun onPointerMoved(position: Offset) {
        rawPosition = position
        if (!isPointerActive) snapPending = true
        isPointerActive = true
        isEngaged = true
    }

    /** A finger lifted. Mouse buttons do not deactivate, since a hovering mouse is still near. */
    fun onPointerReleased(isTouch: Boolean) {
        if (isTouch) isPointerActive = false
    }

    fun onPointerExited() {
        isPointerActive = false
    }

    fun onButtonActivated() {
        frame?.let(effect::onActivate)
    }

    /** Advances everything by [dt] seconds for a stage of [size] with the button laid out at [box] (dp). */
    fun step(dt: Float, size: Size, box: Rect): StageFrame {
        time += dt
        val raw = if (isEngaged) rawPosition else demoPointer(size, box)
        val active = !isEngaged || isPointerActive

        smoothed = if (snapPending) raw.also { snapPending = false } else lerp(smoothed, raw, damp(22f, dt))

        val distance = box.distanceTo(smoothed)
        val radius = min(size.width, size.height) * 0.55f
        val target = if (active) 1f - smoothstep(0f, radius, distance) else 0f
        proximity = lerp(proximity, target, damp(10f, dt))

        val next = StageFrame(
            dt = dt,
            time = time,
            size = size,
            box = box,
            pointer = PointerSample(smoothed, isActive = active, isDemo = !isEngaged),
            distance = distance,
            proximity = proximity,
            motionScale = if (reducedMotion) 0.3f else 1f,
        )
        effect.update(next)
        light = lightRig.update(next)
        frame = next
        return next
    }

    /** Lissajous path that sweeps through the button so the effect is visible before anyone touches it. */
    private fun demoPointer(size: Size, box: Rect): Offset {
        val t = time * if (reducedMotion) 0.3f else 0.6f
        return Offset(
            box.center.x + cos(t) * size.width * 0.3f,
            box.center.y + sin(t * 2f) * size.height * 0.24f,
        )
    }
}
