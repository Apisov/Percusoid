package com.apisov.percusoid.util

class AccelerometerFilter {

    private val gravity = FloatArray(3)
    private val linearAcceleration = FloatArray(3)

    private var dt = 0f
    private var alpha: Float = 0.toFloat()
    private var startTime: Float = 0.toFloat()

    private var count = 1

    private val gravityForce: Float
        get() = Math.abs(
            Math.sqrt(
                Math.pow(
                    gravity[0].toDouble(),
                    2.0
                ) + Math.pow(gravity[1].toDouble(), 2.0) + Math.pow(gravity[2].toDouble(), 2.0)
            )
        ).toFloat()

    private val accelerationForce: Float
        get() = Math.abs(
            Math.sqrt(
                Math.pow(linearAcceleration[0].toDouble(), 2.0)
                        + Math.pow(linearAcceleration[1].toDouble(), 2.0)
                        + Math.pow(linearAcceleration[2].toDouble(), 2.0)
            )
        ).toFloat()

    val magnitude: Float
        get() = accelerationForce / gravityForce

    fun addSamples(vararg acceleration: Float): FloatArray {
        // Initialize the start time.
        if (startTime == 0f) {
            startTime = System.nanoTime().toFloat()
        }

        // event delivery time rate
        dt = 1 / (count++ / ((System.nanoTime() - startTime) / 1000000000.0f))

        alpha = TIME_CONSTANT / (TIME_CONSTANT + dt)

        gravity[0] = alpha * gravity[0] + (1 - alpha) * acceleration[0]
        gravity[1] = alpha * gravity[1] + (1 - alpha) * acceleration[1]
        gravity[2] = alpha * gravity[2] + (1 - alpha) * acceleration[2]

        linearAcceleration[0] = acceleration[0] - gravity[0]
        linearAcceleration[1] = acceleration[1] - gravity[1]
        linearAcceleration[2] = acceleration[2] - gravity[2]

        if (count > 1000) {
            count = 1
            startTime = System.nanoTime().toFloat()
        }

        return linearAcceleration
    }

    companion object {
        // Constants for the low-pass filters
        private const val TIME_CONSTANT = 0.08f
    }
}
