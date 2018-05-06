package com.apisov.percusoid.data.source

import com.apisov.percusoid.data.Input
import com.apisov.percusoid.data.Instrument
import com.apisov.percusoid.data.source.local.entity.InputEntity
import com.apisov.percusoid.data.source.local.entity.InstrumentEntity
import com.apisov.percusoid.data.source.local.entity.InstrumentWithInputs

fun fromInstrumentsEntitiesToInstrumentsList(entities: List<InstrumentWithInputs>) =
    entities.map(::fromInstrumentEntityToInstrument)

fun fromInstrumentEntityToInstrument(entity: InstrumentWithInputs) =
    Instrument(entity.id, entity.title, inputs = fromInputEntitiesToInputsList(entity.inputs))

fun fromInstrumentToInstrumentEntity(instrument: Instrument) = when (instrument.id) {
    null -> InstrumentEntity(title = instrument.name)
    else -> InstrumentEntity(id = instrument.id, title = instrument.name)
}

fun fromInputEntitiesToInputsList(entities: List<InputEntity>) =
    entities.map(::fromInputEntityToInput)

fun fromInputsToInputEntityList(inputs: List<Input>) =
    inputs.map(::fromInputToInputEntity)

fun fromInputEntityToInput(input: InputEntity) =
    Input(
        input.id,
        instrumentId = input.instrumentId,
        sensorSensitivity = input.sensorSensitivity,
        inputSensitivity = input.inputSensitivity,
        inputThreshold = input.inputThreshold,
        note = input.note,
        channel = input.channel,
        control = input.control
    )

fun fromInputToInputEntity(input: Input) = when (input.id) {
    null -> InputEntity(
        title = input.name,
        instrumentId = input.instrumentId!!,
        sensorSensitivity = input.sensorSensitivity,
        inputSensitivity = input.inputSensitivity,
        inputThreshold = input.inputThreshold,
        note = input.note,
        channel = input.channel,
        control = input.control
    )
    else -> InputEntity(
        id = input.id,
        instrumentId = input.instrumentId!!,
        title = input.name,
        sensorSensitivity = input.sensorSensitivity,
        inputSensitivity = input.inputSensitivity,
        inputThreshold = input.inputThreshold,
        note = input.note,
        channel = input.channel,
        control = input.control
    )
}