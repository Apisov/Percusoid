package com.apisov.percusoid.ui.instrumentdetails

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.WindowManager
import com.apisov.percusoid.R
import com.apisov.percusoid.service.AccelerometerService
import com.apisov.percusoid.ui.AbsActivity
import org.koin.android.architecture.ext.getViewModel

class InstrumentDetailsActivity : AbsActivity() {

    companion object {
        const val EXTRA_INSTRUMENT_ID = "instrument_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_container)

        getViewModel<InstrumentDetailsViewModel>(parameters = {
            mapOf(
                EXTRA_INSTRUMENT_ID to intent.getStringExtra(
                    EXTRA_INSTRUMENT_ID
                )
            )
        }).apply {
            openSensorEvent.observe(this@InstrumentDetailsActivity, Observer { })
            instrumentSavedEvent.observe(this@InstrumentDetailsActivity, Observer { finish() })
            instrumentDeletedEvent.observe(this@InstrumentDetailsActivity, Observer { finish() })
            deleteDialogEvent.observe(this@InstrumentDetailsActivity, Observer {
                AlertDialog.Builder(this@InstrumentDetailsActivity)
                    .setMessage("Do you want to delete?")
                    .setPositiveButton("Yes") { _, _ -> deleteInstrument() }
                    .setNegativeButton("No", null)
                    .create()
                    .show()
            })
        }

        startService(Intent(this, AccelerometerService::class.java))
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }


    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, AccelerometerService::class.java))
    }

    override fun getFragment() = InstrumentDetailsFragment.newInstance()
}
