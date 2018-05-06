package com.apisov.percusoid.ui.instruments

import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import com.apisov.percusoid.data.Instrument
import com.apisov.percusoid.data.source.InstrumentsRepository
import com.apisov.percusoid.ui.AbsViewModel
import com.apisov.percusoid.ui.SingleLiveEvent
import com.apisov.percusoid.util.ext.with
import com.apisov.percusoid.util.rx.SchedulerProvider

class InstrumentsViewModel(
    private val instrumentsRepository: InstrumentsRepository,
    private val schedulerProvider: SchedulerProvider
) : AbsViewModel() {

    val instruments: ObservableList<Instrument> = ObservableArrayList()
    val openInstrumentEvent = SingleLiveEvent<String?>()

    override fun start() {
        loadInstruments()
    }

    private fun loadInstruments() {
        run {
            instrumentsRepository
                .getInstruments()
                .with(schedulerProvider)
                .subscribe(
                    {
                        with(instruments) {
                            clear()
                            addAll(it)
                        }
                    },
                    Throwable::printStackTrace
                )
        }
    }
}