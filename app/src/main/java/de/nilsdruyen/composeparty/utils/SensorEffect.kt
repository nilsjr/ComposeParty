package de.nilsdruyen.composeparty.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.accessibility.AccessibilityManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.getSystemService
import kotlinx.coroutines.delay

@Composable
fun rememberShakeState(): State<Boolean> {
    val context = LocalContext.current
    val shakeState = remember { mutableStateOf(false) }

    LaunchedEffect(shakeState.value) {
        if (shakeState.value) {
            delay(1_000)
            shakeState.value = false
        }
    }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService<SensorManager>()!!
        val m = ShakeDetector(object : ShakeDetector.Listener {
            override fun hearShake() {
                shakeState.value = true
            }
        })
        m.start(sensorManager)
        onDispose {
            m.stop()
        }
    }

    return shakeState
}

fun View.hapticFeedback() {
    // do not vibrate if in touch exploration mode
    if (context.isTouchExplorationEnabled()) return

    @Suppress("DEPRECATION", "kotlin:S1874")
    performHapticFeedback(
        HapticFeedbackConstants.LONG_PRESS,
        HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING or HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
    )
}

private fun Context.isTouchExplorationEnabled(): Boolean {
    return getSystemService<AccessibilityManager>()?.isTouchExplorationEnabled ?: false
}

class ShakeDetector(private val listener: Listener) : SensorEventListener {
    /**
     * When the magnitude of total acceleration exceeds this
     * value, the phone is accelerating.
     */
    private var accelerationThreshold = DEFAULT_ACCELERATION_THRESHOLD

    /** Listens for shakes.  */
    interface Listener {
        /** Called on the main thread when the device is shaken.  */
        fun hearShake()
    }

    private val queue = SampleQueue()
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    /**
     * Starts listening for shakes on devices with appropriate hardware.
     * Allowing to set the sensor delay, available values are:
     * SENSOR_DELAY_FASTEST, SENSOR_DELAY_GAME, SENSOR_DELAY_UI, SENSOR_DELAY_NORMAL.
     * @see [SensorManager](https://developer.android.com/reference/android/hardware/SensorManager)
     *
     *
     * @return true if the device supports shake detection.
     */
    /**
     * Starts listening for shakes on devices with appropriate hardware.
     *
     * @return true if the device supports shake detection.
     */
    @JvmOverloads
    fun start(
        sensorManager: SensorManager,
        sensorDelay: Int = SensorManager.SENSOR_DELAY_FASTEST
    ): Boolean {
        // Already started?
        if (accelerometer != null) {
            return true
        }
        accelerometer = sensorManager.getDefaultSensor(
            Sensor.TYPE_ACCELEROMETER
        )

        // If this phone has an accelerometer, listen to it.
        if (accelerometer != null) {
            this.sensorManager = sensorManager
            sensorManager.registerListener(this, accelerometer, sensorDelay)
        }
        return accelerometer != null
    }

    /**
     * Stops listening.  Safe to call when already stopped.  Ignored on devices
     * without appropriate hardware.
     */
    fun stop() {
        if (accelerometer != null) {
            queue.clear()
            sensorManager?.unregisterListener(this, accelerometer)
            sensorManager = null
            accelerometer = null
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val accelerating = isAccelerating(event)
        val timestamp = event.timestamp
        queue.add(timestamp, accelerating)
        if (queue.isShaking) {
            queue.clear()
            listener.hearShake()
        }
    }

    /** Returns true if the device is currently accelerating.  */
    private fun isAccelerating(event: SensorEvent): Boolean {
        val ax = event.values[0]
        val ay = event.values[1]
        val az = event.values[2]

        // Instead of comparing magnitude to ACCELERATION_THRESHOLD,
        // compare their squares. This is equivalent and doesn't need the
        // actual magnitude, which would be computed using (expensive) Math.sqrt().
        val magnitudeSquared = (ax * ax + ay * ay + az * az).toDouble()
        return magnitudeSquared > accelerationThreshold * accelerationThreshold
    }

    /** Sets the acceleration threshold sensitivity.  */
    fun setSensitivity(accelerationThreshold: Int) {
        this.accelerationThreshold = accelerationThreshold
    }

    /** Queue of samples. Keeps a running average.  */
    internal class SampleQueue {
        private val pool = SamplePool()
        private var oldest: Sample? = null
        private var newest: Sample? = null
        private var sampleCount = 0
        private var acceleratingCount = 0

        /**
         * Adds a sample.
         *
         * @param timestamp    in nanoseconds of sample
         * @param accelerating true if > [.accelerationThreshold].
         */
        fun add(timestamp: Long, accelerating: Boolean) {
            // Purge samples that proceed window.
            purge(timestamp - MAX_WINDOW_SIZE)

            // Add the sample to the queue.
            val added = pool.acquire()
            added.timestamp = timestamp
            added.accelerating = accelerating
            added.next = null
            if (newest != null) {
                newest!!.next = added
            }
            newest = added
            if (oldest == null) {
                oldest = added
            }

            // Update running average.
            sampleCount++
            if (accelerating) {
                acceleratingCount++
            }
        }

        /** Removes all samples from this queue.  */
        fun clear() {
            while (oldest != null) {
                val removed: Sample = oldest as Sample
                oldest = removed.next
                pool.release(removed)
            }
            newest = null
            sampleCount = 0
            acceleratingCount = 0
        }

        /** Purges samples with timestamps older than cutoff.  */
        fun purge(cutoff: Long) {
            while (sampleCount >= MIN_QUEUE_SIZE && oldest != null && cutoff - oldest!!.timestamp > 0) {
                // Remove sample.
                val removed: Sample = oldest as Sample
                if (removed.accelerating) {
                    acceleratingCount--
                }
                sampleCount--
                oldest = removed.next
                if (oldest == null) {
                    newest = null
                }
                pool.release(removed)
            }
        }

        /** Copies the samples into a list, with the oldest entry at index 0.  */
        fun asList(): List<Sample> {
            val list: MutableList<Sample> = ArrayList()
            var s = oldest
            while (s != null) {
                list.add(s)
                s = s.next
            }
            return list
        }

        val isShaking: Boolean
            /**
             * Returns true if we have enough samples and more than 3/4 of those samples
             * are accelerating.
             */
            get() = newest != null && oldest != null && newest!!.timestamp - oldest!!.timestamp >= MIN_WINDOW_SIZE && acceleratingCount >= (sampleCount shr 1) + (sampleCount shr 2)

        companion object {
            /** Window size in ns. Used to compute the average.  */
            private const val MAX_WINDOW_SIZE: Long = 500000000 // 0.5s
            private const val MIN_WINDOW_SIZE = MAX_WINDOW_SIZE shr 1 // 0.25s

            /**
             * Ensure the queue size never falls below this size, even if the device
             * fails to deliver this many events during the time window. The LG Ally
             * is one such device.
             */
            private const val MIN_QUEUE_SIZE = 4
        }
    }

    /** An accelerometer sample.  */
    internal class Sample {
        /** Time sample was taken.  */
        var timestamp: Long = 0

        /** If acceleration > [.accelerationThreshold].  */
        var accelerating = false

        /** Next sample in the queue or pool.  */
        var next: Sample? = null
    }

    /** Pools samples. Avoids garbage collection.  */
    internal class SamplePool {
        private var head: Sample? = null

        /** Acquires a sample from the pool.  */
        fun acquire(): Sample {
            var acquired = head
            if (acquired == null) {
                acquired = Sample()
            } else {
                // Remove instance from pool.
                head = acquired.next
            }
            return acquired
        }

        /** Returns a sample to the pool.  */
        fun release(sample: Sample) {
            sample.next = head
            head = sample
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    companion object {
        const val SENSITIVITY_LIGHT = 11
        const val SENSITIVITY_MEDIUM = 13
        const val SENSITIVITY_HARD = 15
        private const val DEFAULT_ACCELERATION_THRESHOLD = SENSITIVITY_MEDIUM
    }
}