package de.nilsdruyen.composeparty.buttons.sticky.engine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import de.nilsdruyen.composeparty.buttons.sticky.core.damp
import de.nilsdruyen.composeparty.buttons.sticky.core.lerp

enum class LightMode { FollowPointer, Fixed }

/**
 * User-facing light settings.
 *
 * @property x horizontal position as a fraction of the stage width (fixed or resting light)
 * @property y vertical position as a fraction of the stage height
 * @property intensity multiplier for the movable light, 0..2
 * @property height light height above the stage in dp; higher spreads the highlight
 * @property key strength of the fixed top-left studio light, 0..1.5
 */
data class LightSettings(
    val mode: LightMode = LightMode.FollowPointer,
    val x: Float = 0.18f,
    val y: Float = 0.12f,
    val intensity: Float = 1f,
    val height: Float = 260f,
    val key: Float = 1f,
) {
    companion object {
        val Default = LightSettings()
    }
}

/** The light as it should be rendered this frame (position in dp). */
data class LightState(
    val position: Offset,
    val strength: Float,
    val height: Float,
    val key: Float,
    val isFollowing: Boolean,
)

/** Owns the light settings and turns them into a smoothed [LightState] for each frame. */
class LightRig(initial: LightSettings = LightSettings.Default) {

    /** Observable so the light panel recomposes when settings change. */
    var settings: LightSettings by mutableStateOf(initial)

    private var position = Offset.Zero
    private var strength = IDLE_STRENGTH
    private var isInitialized = false

    fun reset() {
        settings = LightSettings.Default
    }

    fun update(frame: StageFrame): LightState {
        val current = settings
        val isFollowing = current.mode == LightMode.FollowPointer && frame.pointer.isActive
        val targetPosition = if (isFollowing) {
            frame.pointer.position
        } else {
            Offset(current.x * frame.size.width, current.y * frame.size.height)
        }
        val targetStrength = when {
            isFollowing -> 1f
            current.mode == LightMode.Fixed -> 1f
            else -> IDLE_STRENGTH
        }

        val rate = if (isInitialized) damp(if (isFollowing) 14f else 5f, frame.dt) else 1f
        position = lerp(position, targetPosition, rate)
        strength = lerp(strength, targetStrength, rate)
        isInitialized = true

        return LightState(
            position = position,
            strength = strength * current.intensity,
            height = current.height,
            key = current.key,
            isFollowing = isFollowing,
        )
    }

    private companion object {
        const val IDLE_STRENGTH = 0.25f
    }
}
