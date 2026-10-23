package de.nilsdruyen.composeparty.buttons.sticky.engine

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import de.nilsdruyen.composeparty.buttons.sticky.core.angleTo

/** Where the pointer is this frame. [isDemo] marks the scripted pointer shown before first use. */
data class PointerSample(val position: Offset, val isActive: Boolean, val isDemo: Boolean)

/**
 * Immutable snapshot handed to effects every frame. All lengths are in dp.
 *
 * @property proximity 0 when far away or absent, 1 when touching the button; eased over time.
 * @property motionScale 1 normally, smaller when the user prefers reduced motion.
 */
class StageFrame(
    val dt: Float,
    val time: Float,
    val size: Size,
    val box: Rect,
    val pointer: PointerSample,
    val distance: Float,
    val proximity: Float,
    val motionScale: Float,
) {
    val center: Offset get() = box.center

    /** Angle from the button centre toward the pointer. */
    val aim: Float get() = box.angleTo(pointer.position)
}
