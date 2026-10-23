package de.nilsdruyen.composeparty.buttons.sticky.core

import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

/**
 * Idle life for organic liquids: a slow left/right sway, breathing and random twitches.
 * Shared by every "living" effect so they all pulse with the same character.
 */
class LifePulse(private val random: Random = Random.Default) {
    private val seed = random.nextFloat() * 100f
    private var twitch = 0f
    private var twitchTarget = 0f
    private var timer = 1f

    fun update(dt: Float) {
        timer -= dt
        if (timer <= 0f) {
            timer = 0.6f + random.nextFloat() * 1.6f
            twitchTarget = (random.nextFloat() * 2f - 1f) * 1.3f
        }
        twitchTarget *= exp(-dt * 1.5f)
        twitch = lerp(twitch, twitchTarget, damp(3f, dt))
    }

    /** Signed lateral drift in -1..1: positive leans right, negative leans left. */
    fun sway(time: Float): Float = (noise1D(time * 0.6f + seed) * 0.8f + twitch).coerceIn(-1f, 1f)

    /** Outward surface offset (dp) for an outline point at [angle]. */
    fun offsetAt(angle: Float, time: Float, motion: Float): Float {
        // Lateral lean: one side swells while the other pulls in, so the body visibly shifts left/right.
        val lateral = sway(time) * 12f * cos(angle)
        // Breathing concentrated on the long sides, plus a slow wave crawling around the outline.
        val breathing = 3.5f * sin(time * 1.7f + seed) * (0.5f + 0.5f * cos(2f * angle))
        val crawl = 2.5f * sin(3f * angle - time * 2.2f + seed)
        return (lateral + breathing + crawl) * motion
    }
}
