package com.apisov.percusoid.data

data class Input(
    val id: String? = null,
    var instrumentId: String? = null,
    val name: String = "",
    var sensorSensitivity: Int = 50,
    var inputSensitivity: Int = 80,
    var inputThreshold: Int = 30,
    var note: Int = 64,
    var channel: Int = 1,
    var control: Int = 0
)