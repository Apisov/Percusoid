package com.apisov.percusoid.data.source.local.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "Instruments")
open class InstrumentEntity(
    var title: String = "",
    var description: String = "",
    @PrimaryKey var id: String = UUID.randomUUID().toString()
)