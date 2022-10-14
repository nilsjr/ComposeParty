package de.nilsdruyen.composeparty.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.ui.geometry.Offset
import androidx.core.content.getSystemService
import timber.log.Timber

class SensorManager constructor(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService<SensorManager>()!!
    private val accelerometer: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private var startDeviceOrientation: DeviceData? = null
    var onChangeListener: (DeviceData) -> Unit = {}

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        if (startDeviceOrientation != null) {
            val changed = DeviceData(
                x = event.values[0],
                y = event.values[1],
                z = event.values[2]
            ) - (startDeviceOrientation ?: DefaultDeviceData)
            onChangeListener(changed)
        } else {
            startDeviceOrientation = DeviceData(event.values[0], event.values[1], event.values[2])
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun start() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }
}

data class DeviceData(
    val x: Float,
    val y: Float,
    val z: Float,
)

val DefaultDeviceData = DeviceData(0f, 0f, 0f)

operator fun DeviceData.minus(other: DeviceData): DeviceData {
    return DeviceData(this.x - other.x, this.y - other.y, this.z - other.z)
}

data class ObjectMovement(val x: Int, val y: Int)

fun DeviceData.map(): ObjectMovement = ObjectMovement((x * 10).toInt(), (y * 10).toInt())
fun DeviceData.toOffset(): Offset = Offset(x * 10, y * 10)