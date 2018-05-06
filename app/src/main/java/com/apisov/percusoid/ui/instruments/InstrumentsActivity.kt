package com.apisov.percusoid.ui.instruments

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import com.apisov.percusoid.R
import com.apisov.percusoid.ui.AbsActivity
import com.apisov.percusoid.ui.instrumentdetails.InstrumentDetailsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.android.architecture.ext.getViewModel

class InstrumentsActivity : AbsActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitle(R.string.app_name)

        getViewModel<InstrumentsViewModel>().apply {
            openInstrumentEvent.observe(this@InstrumentsActivity, Observer {
                openInstrument(it)
            })
        }

        btnCreateInstrument.setOnClickListener { openInstrument(null) }
    }

    private fun openInstrument(instrumentId: String?) {
        val intent = Intent(
            this@InstrumentsActivity,
            InstrumentDetailsActivity::class.java
        ).apply {
            putExtra(InstrumentDetailsActivity.EXTRA_INSTRUMENT_ID, instrumentId)
        }
        startActivity(intent)
    }

    override fun getFragment() = InstrumentsFragment.newInstance()
}
