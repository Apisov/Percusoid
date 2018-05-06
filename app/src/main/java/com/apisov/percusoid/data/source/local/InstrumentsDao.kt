package com.apisov.percusoid.data.source.local

import android.arch.persistence.room.*
import com.apisov.percusoid.data.source.local.entity.InstrumentEntity
import com.apisov.percusoid.data.source.local.entity.InstrumentWithInputs
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface InstrumentsDao {

    @Transaction
    @Query("SELECT * FROM Instruments")
    fun getInstruments(): Flowable<List<InstrumentWithInputs>>

    @Transaction
    @Query("SELECT * FROM Instruments WHERE id = :id")
    fun getInstrumentById(id: String): Single<InstrumentWithInputs>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveInstrument(instrument: InstrumentEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateInstrument(instrument: InstrumentEntity) : Int

    @Query("DELETE FROM Instruments WHERE id = :instrumentId")
    fun deleteInstrumentById(instrumentId: String): Int
}