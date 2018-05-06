package com.apisov.percusoid.di

import android.arch.persistence.room.Room
import com.apisov.percusoid.data.source.InputsRepository
import com.apisov.percusoid.data.source.InstrumentsRepository
import com.apisov.percusoid.data.source.local.PercusoidDatabase
import com.apisov.percusoid.ui.instrumentdetails.InstrumentDetailsActivity
import com.apisov.percusoid.ui.instrumentdetails.InstrumentDetailsViewModel
import com.apisov.percusoid.ui.instruments.InstrumentsViewModel
import com.apisov.percusoid.util.rx.AppSchedulerProvider
import com.apisov.percusoid.util.rx.SchedulerProvider
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext

val percusoidModule = applicationContext {
    viewModel { InstrumentsViewModel(get(), get()) }

    viewModel { params ->
        InstrumentDetailsViewModel(
            params[InstrumentDetailsActivity.EXTRA_INSTRUMENT_ID],
            get(),
            get(),
            get()
        )
    }

    bean { InstrumentsRepository(get()) }
    bean { InputsRepository(get()) }
    bean {
        Room.databaseBuilder(
            androidApplication(),
            PercusoidDatabase::class.java,
            "Percusoid.db"
        ).build()
    }
}

val rxModule = applicationContext {
    bean { AppSchedulerProvider() as SchedulerProvider }
}

val percusoidApp = listOf(percusoidModule, rxModule)