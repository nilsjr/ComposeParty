package de.nilsdruyen.composeparty.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.ui.geometry.Offset
import androidx.core.content.getSystemService

class SensorManager constructor(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService<SensorManager>()!!
    private val gravitySensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    private val magneticSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    private var gravity: FloatArray? = null
    private var geomagnetic: FloatArray? = null

    var onChangeListener: (Offset) -> Unit = {}

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        if (event.sensor?.type == Sensor.TYPE_GRAVITY) gravity = event.values
        if (event.sensor?.type == Sensor.TYPE_MAGNETIC_FIELD) geomagnetic = event.values
        if (gravity != null && geomagnetic != null) {
            val r = FloatArray(9)
            val i = FloatArray(9)
            if (SensorManager.getRotationMatrix(r, i, gravity, geomagnetic)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)
                onChangeListener(Offset(orientation[2] * 10, orientation[1] * 10))
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun start() {
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }
}