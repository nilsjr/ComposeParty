package de.nilsdruyen.composeparty.buttons.sticky.core

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow

/** Full turn in radians. */
const val TAU: Float = (PI * 2).toFloat()

fun lerp(a: Float, b: Float, t: Float): Float = a + (b - a) * t

fun lerp(a: Offset, b: Offset, t: Float): Offset = Offset(lerp(a.x, b.x, t), lerp(a.y, b.y, t))

fun smoothstep(edge0: Float, edge1: Float, x: Float): Float {
    val t = ((x - edge0) / (edge1 - edge0)).coerceIn(0f, 1f)
    return t * t * (3f - 2f * t)
}

/** Exponential smoothing factor for [rate] per second, independent of frame rate. */
fun damp(rate: Float, dt: Float): Float = 1f - exp(-rate * dt)

fun easeOutCubic(t: Float): Float = 1f - (1f - t).pow(3)

fun easeInOutCubic(t: Float): Float =
    if (t < 0.5f) 4f * t * t * t else 1f - (-2f * t + 2f).pow(3) / 2f
