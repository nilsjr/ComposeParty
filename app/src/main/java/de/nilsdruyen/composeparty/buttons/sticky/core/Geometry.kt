package de.nilsdruyen.composeparty.buttons.sticky.core

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sqrt

/** Distance from [point] to the outside of this rectangle; 0 when the point is inside. */
fun Rect.distanceTo(point: Offset): Float {
    val dx = max(max(left - point.x, 0f), point.x - right)
    val dy = max(max(top - point.y, 0f), point.y - bottom)
    return hypot(dx, dy)
}

/** Angle from the rectangle centre toward [point], in radians. */
fun Rect.angleTo(point: Offset): Float = atan2(point.y - center.y, point.x - center.x)

/**
 * Distance from the centre to the pill-shaped (stadium) outline inscribed in this rectangle,
 * along [angle]. The corner radius is half the shorter side, so both orientations work.
 */
fun Rect.stadiumRadius(angle: Float): Float {
    val radius = min(width, height) / 2f
    val halfStraightX = width / 2f - radius
    val halfStraightY = height / 2f - radius
    val c = cos(angle)
    val s = sin(angle)

    if (abs(s) > 1e-6f) {
        val t = (halfStraightY + radius) / abs(s)
        if (abs(t * c) <= halfStraightX) return t
    }
    if (abs(c) > 1e-6f) {
        val t = (halfStraightX + radius) / abs(c)
        if (abs(t * s) <= halfStraightY) return t
    }
    val capX = sign(c) * halfStraightX
    val capY = sign(s) * halfStraightY
    val dot = c * capX + s * capY
    val discriminant = dot * dot - (capX * capX + capY * capY) + radius * radius
    return dot + sqrt(max(0f, discriminant))
}

/** Point on the pill outline at [angle], optionally pulled [inset] toward the centre. */
fun Rect.edgePoint(angle: Float, inset: Float = 0f): Offset {
    val r = stadiumRadius(angle) - inset
    return Offset(center.x + cos(angle) * r, center.y + sin(angle) * r)
}

/** True when [point] lies inside the pill outline grown by [tolerance]. */
fun Rect.stadiumContains(point: Offset, tolerance: Float = 0f): Boolean {
    val distance = (point - center).getDistance()
    return distance <= stadiumRadius(angleTo(point)) + tolerance
}
