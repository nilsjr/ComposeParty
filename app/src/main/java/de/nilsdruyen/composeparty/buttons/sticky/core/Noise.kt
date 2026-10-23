package de.nilsdruyen.composeparty.buttons.sticky.core

import kotlin.math.floor
import kotlin.math.sin

/** Smooth 1D value noise in [-1, 1] for cheap organic wobble. */
fun noise1D(x: Float): Float {
    fun hash(n: Float): Float {
        val s = sin(n * 127.1f) * 43758.5453f
        return s - floor(s)
    }
    val i = floor(x)
    val f = x - i
    val u = f * f * (3f - 2f * f)
    return lerp(hash(i), hash(i + 1f), u) * 2f - 1f
}
