package de.nilsdruyen.composeparty.freestyle.explodable

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.MutableRect
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun rememberExplosionController() = remember { ExplosionController() }

internal enum class ExplosionRequest {
    EXPLODE, RESET
}

class ExplosionController {
    private val _explosionRequests = MutableSharedFlow<ExplosionRequest>(
        replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_LATEST
    )
    internal val explosionRequests: SharedFlow<ExplosionRequest> = _explosionRequests

    fun explode() {
        _explosionRequests.tryEmit(ExplosionRequest.EXPLODE)
    }

    fun reset() {
        _explosionRequests.tryEmit(ExplosionRequest.RESET)
    }
}

fun ImageBitmap.getPixel(x: Int, y: Int) = IntArray(1).apply {
    readPixels(this, startX = x, startY = y, width = 1, height = 1)
}.first()

fun Float.mapInRange(inMin: Float, inMax: Float, outMin: Float, outMax: Float): Float {
    return outMin + (((this - inMin) / (inMax - inMin)) * (outMax - outMin))
}

fun Int.dpToPx() = toFloat().dpToPx()
fun Dp.toPx() = value.dpToPx()

fun Float.dpToPx() = this * Resources.getSystem().displayMetrics.density

val Rect.centerX get() = left + (width / 2)
val Rect.centerY get() = top + (height / 2)

fun Rect.mutate(block: MutableRect.() -> Unit) = MutableRect(left, top, right, bottom).apply {
    block(this)
}

fun MutableRect.offset(dx: Float, dy: Float) {
    left += dx
    top += dy
    right += dx
    bottom += dy
}

fun MutableRect.scale(factor: Float) {
    val oldWidth = width
    val oldHeight = height
    val rectCenterX = left + oldWidth / 2F
    val rectCenterY = top + oldHeight / 2F
    val newWidth = oldWidth * factor
    val newHeight = oldHeight * factor
    left = rectCenterX - newWidth / 2F
    right = rectCenterX + newWidth / 2F
    top = rectCenterY - newHeight / 2F
    bottom = rectCenterY + newHeight / 2F
}

private val random = java.util.Random()
fun Float.randomTillZero() = this * random.nextFloat()
fun randomInRange(min: Float, max: Float) = min + (max - min).randomTillZero()
fun randomBoolean(trueProbabilityPercentage: Int) =
    random.nextFloat() < trueProbabilityPercentage / 100f