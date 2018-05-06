package com.apisov.percusoid.data.source.local

import android.arch.persistence.room.*
import com.apisov.percusoid.data.source.local.entity.InputEntity
import io.reactivex.Single

@Dao
interface InputsDao {

    @Transaction
    @Query("SELECT * FROM Inputs WHERE id = :id")
    fun getInputById(id: String): Single<InputEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveInput(input: InputEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveInputs(inputs: List<InputEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateInputs(inputs: List<InputEntity>)

    @Query("DELETE FROM Inputs WHERE id = :inputId")
    fun deleteInputById(inputId: String): Int
}