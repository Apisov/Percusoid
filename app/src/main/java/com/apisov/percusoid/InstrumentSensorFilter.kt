package com.apisov.percusoid

const val MIDI_MAX_VELOCITY = 127

class InstrumentSensorFilter {

    var maxValue = 6f
        set(value) {
            field = value
            scaleCoef = MIDI_MAX_VELOCITY / (Math.round(value * 100f) / 100f)
        }

    var threshold = 0f

    private var scaleCoef = MIDI_MAX_VELOCITY / (Math.round(maxValue * 100f) / 100f)

    fun magnitudeToVelocity(magnitude: Float): Int {
        var velocity = (magnitude * scaleCoef).toInt()

        if (velocity > MIDI_MAX_VELOCITY) velocity = MIDI_MAX_VELOCITY
        return velocity
    }
}