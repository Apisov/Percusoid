package com.apisov.percusoid.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.apisov.percusoid.util.AccelerometerFilter
import com.apisov.percusoid.util.SensorEventBus

const val ACCELEROMETER_SAMPLING_PERIOD = 10L
const val AVERAGE_BUFFER = 3
const val GRAVITY_CALCULATION_OFFSET = 150

class AccelerometerService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var filter: AccelerometerFilter

    private var passedSensorSamples: Int = 0
    private var timeSinceLastMagnitudeUpdate: Long = 0

    override fun onCreate() {
        super.onCreate()
        filter = AccelerometerFilter()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorManager.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_FASTEST
        )
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?) = null
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        event?.apply {
            filter.addSamples(
                values[0],
                values[1],
                values[2]
            )
        }

        if (passedSensorSamples > GRAVITY_CALCULATION_OFFSET) {
            if (System.currentTimeMillis() - timeSinceLastMagnitudeUpdate >= ACCELEROMETER_SAMPLING_PERIOD) {
                timeSinceLastMagnitudeUpdate = System.currentTimeMillis()
                SensorEventBus.post(filter.magnitude)
            }
        } else {
            passedSensorSamples++
        }
    }
}