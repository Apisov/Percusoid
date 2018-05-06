package com.apisov.percusoid.data.source.local.entity

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import java.util.*


@Entity(
    tableName = "Inputs",
    foreignKeys = [ForeignKey(
        entity = InstrumentEntity::class,
        parentColumns = ["id"],
        childColumns = ["instrument_id"],
        onUpdate = CASCADE,
        onDelete = CASCADE
    )],
    indices = [Index("instrument_id")]
)
class InputEntity(
    @PrimaryKey var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var description: String = "",
    @ColumnInfo(name = "sensor_sensitivity") var sensorSensitivity: Int = 50,
    @ColumnInfo(name = "input_sensitivity") var inputSensitivity: Int = 80,
    @ColumnInfo(name = "input_threshold") var inputThreshold: Int = 30,
    @ColumnInfo(name = "instrument_id") var instrumentId: String = "0",
    var note: Int = 64,
    var channel: Int = 1,
    var control: Int = 0
)