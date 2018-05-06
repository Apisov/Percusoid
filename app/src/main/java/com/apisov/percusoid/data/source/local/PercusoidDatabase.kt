package com.apisov.percusoid.data.source.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.apisov.percusoid.data.source.local.entity.InputEntity
import com.apisov.percusoid.data.source.local.entity.InstrumentEntity

@Database(entities = [InstrumentEntity::class, InputEntity::class], version = 1)
abstract class PercusoidDatabase : RoomDatabase() {

    abstract fun instrumentsDao(): InstrumentsDao

    abstract fun inputsDao(): InputsDao
}