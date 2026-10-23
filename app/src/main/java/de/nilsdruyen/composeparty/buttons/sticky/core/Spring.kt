package de.nilsdruyen.composeparty.buttons.sticky.core

import androidx.compose.ui.geometry.Offset
import kotlin.math.ceil

/** A point mass driven by damped springs. */
class SpringBody(var position: Offset, var velocity: Offset = Offset.Zero) {

    /** Pulls the body toward [target]; sub-stepped at 120 Hz so stiff springs stay stable. */
    fun springToward(target: Offset, stiffness: Float, damping: Float, dt: Float) {
        val substeps = ceil(dt / SUBSTEP).toInt().coerceAtLeast(1)
        val step = dt / substeps
        repeat(substeps) {
            val acceleration = (target - position) * stiffness - velocity * damping
            velocity += acceleration * step
            position += velocity * step
        }
    }

    private companion object {
        const val SUBSTEP = 1f / 120f
    }
}
