package com.apisov.percusoid.ui.instrumentdetails

import android.databinding.ObservableField
import com.apisov.percusoid.InstrumentSensorFilter
import com.apisov.percusoid.OscManager
import com.apisov.percusoid.data.Input
import com.apisov.percusoid.data.Instrument
import com.apisov.percusoid.data.source.InputsRepository
import com.apisov.percusoid.data.source.InstrumentsRepository
import com.apisov.percusoid.service.AVERAGE_BUFFER
import com.apisov.percusoid.ui.AbsViewModel
import com.apisov.percusoid.ui.SingleLiveEvent
import com.apisov.percusoid.util.SensorEventBus
import com.apisov.percusoid.util.ext.with
import com.apisov.percusoid.util.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class InstrumentDetailsViewModel(
    private val instrumentId: String?,
    private val instrumentsRepository: InstrumentsRepository,
    private val inputsRepository: InputsRepository,
    private val schedulerProvider: SchedulerProvider
) : AbsViewModel() {

    val instrument: ObservableField<Instrument> = ObservableField()
    val input: ObservableField<Input> = ObservableField()
    val openSensorEvent = SingleLiveEvent<String>()
    val instrumentSavedEvent = SingleLiveEvent<String>()
    val instrumentDeletedEvent = SingleLiveEvent<Unit>()
    val deleteDialogEvent = SingleLiveEvent<Unit>()
    val onClearedEvent = SingleLiveEvent<String>()
    val onHitEvent = SingleLiveEvent<Int>()

    private lateinit var oscManager: OscManager
    private var instrumentName: String = ""

    private val compositeDisposable = CompositeDisposable()

    private val instrumentSensorFilter = InstrumentSensorFilter()

    override fun start() {
        oscManager = OscManager()

        if (instrumentId != null) {
            run {
                instrumentsRepository
                    .getInstrumentById(instrumentId)
                    .with(schedulerProvider)
                    .subscribe({
                        input.set(it.inputs.first())
                        instrument.set(it)
                        addSensorObserver()
                    }, Throwable::printStackTrace)
            }
        } else {
            val instr = Instrument()
            instrument.set(instr)
            input.set(instr.inputs.first())
            addSensorObserver()
        }
    }

    private fun addSensorObserver() {
        compositeDisposable.add(SensorEventBus.subject
            .buffer(AVERAGE_BUFFER, 1)
            .map { it.average().toFloat() }
            .doOnNext {
                if (it > instrumentSensorFilter.threshold) {
                    if (oscManager.connected) {
                        oscManager.sendControls(
                            input.get()!!.channel,
                            input.get()!!.control,
                            instrumentSensorFilter.magnitudeToVelocity(it)
                        )
                    }
                }
            }
            .distinctUntilChanged { value -> value > instrumentSensorFilter.threshold }
            .filter { it > instrumentSensorFilter.threshold }
            .map(instrumentSensorFilter::magnitudeToVelocity)
            .with(schedulerProvider)
            .subscribe {
                onHitEvent.value = it

                if (oscManager.connected) {
                    oscManager.hitPercussion(
                        input.get()!!.channel,
                        input.get()!!.note,
                        it
                    )
                }
            })
    }

    fun onSaveClicked() {
        saveInstrument()
    }

    private fun saveInstrument() {
        run {
            if (instrumentId == null) {
                instrumentsRepository
                    .saveInstrument(Instrument(name = instrumentName))
                    .flatMap { instrumentId ->
                        inputsRepository.saveInputs(
                            instrument.get()!!.inputs.map {
                                it.instrumentId = instrumentId
                                it
                            })
                    }
                    .with(schedulerProvider)
                    .subscribe({ instrumentSavedEvent.call() }, Throwable::printStackTrace)
            } else {
                instrumentsRepository
                    .updateInstrument(instrument.get()!!)
                    .flatMap { inputsRepository.updateInputs(instrument.get()!!.inputs) }
                    .with(schedulerProvider)
                    .subscribe({ }, Throwable::printStackTrace)
            }
        }
    }

    fun onDeleteClicked() {
        deleteDialogEvent.call()
    }

    fun deleteInstrument() {
        run {
            instrumentsRepository.deleteInstrument(instrumentId!!)
                .with(schedulerProvider)
                .subscribe({ instrumentDeletedEvent.call() }, Timber::d)
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        onClearedEvent.call()
    }

    fun setName(name: String) {
        if (instrumentName != name) {
            instrumentName = name
            val tmpInstrument = instrument.get()
            if (tmpInstrument != null) {
                instrument.set(tmpInstrument.copy(id = instrumentId, name = name))
            }
        }
    }

    fun onInstrumentSensitivityChanged(progress: Int) {
        input.get()?.inputSensitivity = progress
    }

    fun onInstrumentThresholdChanged(progress: Int) {
        input.get()?.inputThreshold = progress
    }

    fun onSensorSensitivityChanged(progress: Int) {
        input.get()?.sensorSensitivity = progress
    }

    fun updateSensorFilter(maxValue: Float, instrumentThreshold: Float) {
        instrumentSensorFilter.threshold = instrumentThreshold
        instrumentSensorFilter.maxValue = maxValue
    }

    fun onNoteChanged(note: Int) {
        input.get()?.note = note
    }

    fun onChannelChanged(channel: Int) {
        input.get()?.channel = channel - 1 /* Midi channel info is 0-15 */
    }

    fun onControlChanged(control: Int) {
        input.get()?.control = control
    }

    fun resume() {
        oscManager.connect()
    }

    fun pause() {
        saveInstrument()
        oscManager.disconnect(object : OscManager.OnSendCompleteCallback {
            override fun onComplete() {
                oscManager.stop()
            }
        })
    }
}