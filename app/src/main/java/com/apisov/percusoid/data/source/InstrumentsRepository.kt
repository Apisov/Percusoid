package com.apisov.percusoid.data.source

import com.apisov.percusoid.data.Instrument
import com.apisov.percusoid.data.source.local.PercusoidDatabase
import io.reactivex.Flowable
import io.reactivex.Single

class InstrumentsRepository(
    private val database: PercusoidDatabase
) {
    fun saveInstrument(instrument: Instrument): Single<String> =
        Single.defer {
            val instrumentEntity = fromInstrumentToInstrumentEntity(instrument)
            Single.just(
                database.instrumentsDao().saveInstrument(instrumentEntity)
            ).map { instrumentEntity.id }
        }

    fun updateInstrument(instrument: Instrument): Single<Int> =
        Single.defer {
            Single.just(
                database.instrumentsDao().updateInstrument(
                    fromInstrumentToInstrumentEntity(
                        instrument
                    )
                )
            )
        }

    fun deleteInstrument(instrumentId: String): Single<Int> = Single.defer {
        Single.just(
            database.instrumentsDao().deleteInstrumentById(instrumentId)
        )
    }

    fun getInstrumentById(instrumentId: String): Single<Instrument> =
        database.instrumentsDao().getInstrumentById(instrumentId).map(::fromInstrumentEntityToInstrument)

    fun getInstruments(): Flowable<List<Instrument>> =
        database.instrumentsDao().getInstruments().map(::fromInstrumentsEntitiesToInstrumentsList)
}