package com.apisov.percusoid.data.source.local.entity

import android.arch.persistence.room.Relation

class InstrumentWithInputs : InstrumentEntity() {

    @Relation(parentColumn = "id", entityColumn = "instrument_id")
    var inputs: List<InputEntity> = emptyList()
}