package de.nilsdruyen.composeparty.buttons.sticky.render

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import de.nilsdruyen.composeparty.buttons.sticky.core.TAU
import de.nilsdruyen.composeparty.buttons.sticky.core.stadiumRadius
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/** Fills a pill whose surface is pushed outward by [offsetAt] (angle → dp): the living skin of a liquid. */
fun DrawScope.fillOrganicStadium(box: Rect, color: Color, samples: Int = 96, offsetAt: (angle: Float) -> Float) {
    val points = List(samples) { k ->
        val angle = k.toFloat() / samples * TAU
        val r = box.stadiumRadius(angle) + offsetAt(angle)
        Offset(box.center.x + cos(angle) * r, box.center.y + sin(angle) * r)
    }
    drawPath(smoothLoop(points), color)
}

/** Closed smooth path through [points] using quadratic curves between midpoints. */
fun smoothLoop(points: List<Offset>): Path = Path().apply {
    val count = points.size
    val start = (points[count - 1] + points[0]) / 2f
    moveTo(start.x, start.y)
    for (i in 0 until count) {
        val next = (points[i] + points[(i + 1) % count]) / 2f
        quadraticTo(points[i].x, points[i].y, next.x, next.y)
    }
    close()
}

/** Samples a Catmull-Rom spline through [points] so coarse simulations render as smooth curves. */
fun sampleSpline(points: List<Offset>, perSegment: Int = 3): List<Offset> {
    val last = points.lastIndex
    val out = ArrayList<Offset>(last * perSegment + 1)
    for (i in 0 until last) {
        val p0 = points[max(i - 1, 0)]
        val p1 = points[i]
        val p2 = points[i + 1]
        val p3 = points[min(i + 2, last)]
        for (s in 0 until perSegment) {
            val t = s.toFloat() / perSegment
            val t2 = t * t
            val t3 = t2 * t
            fun blend(a: Float, b: Float, c: Float, d: Float) =
                0.5f * (2f * b + (-a + c) * t + (2f * a - 5f * b + 4f * c - d) * t2 + (-a + 3f * b - 3f * c + d) * t3)
            out += Offset(blend(p0.x, p1.x, p2.x, p3.x), blend(p0.y, p1.y, p2.y, p3.y))
        }
    }
    out += points[last]
    return out
}

/** Fills a ribbon along [path] whose width is [widthAt] (0..1 along the path), with round caps. */
fun DrawScope.fillRibbon(path: List<Offset>, color: Color, widthAt: (Float) -> Float) {
    val count = path.size
    if (count < 2) return
    val left = ArrayList<Offset>(count)
    val right = ArrayList<Offset>(count)
    for (i in 0 until count) {
        val prev = path[max(i - 1, 0)]
        val next = path[min(i + 1, count - 1)]
        val dx = next.x - prev.x
        val dy = next.y - prev.y
        val length = hypot(dx, dy).takeIf { it > 0f } ?: 1f
        val half = widthAt(i.toFloat() / (count - 1)) / 2f
        val normal = Offset(-dy / length, dx / length) * half
        left += path[i] + normal
        right += path[i] - normal
    }
    val ribbon = Path().apply {
        moveTo(left[0].x, left[0].y)
        left.forEach { lineTo(it.x, it.y) }
        for (i in count - 1 downTo 0) lineTo(right[i].x, right[i].y)
        close()
    }
    drawPath(ribbon, color)
    drawCircle(color, widthAt(0f) / 2f, path.first())
    drawCircle(color, widthAt(1f) / 2f, path.last())
}
