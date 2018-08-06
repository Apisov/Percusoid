package com.apisov.percusoid

import android.app.Application
import com.apisov.percusoid.di.percusoidApp
import org.koin.android.ext.android.startKoin
import timber.log.Timber

class PercusoidApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(percusoidApp)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}