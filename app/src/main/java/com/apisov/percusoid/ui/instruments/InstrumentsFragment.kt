package com.apisov.percusoid.ui.instruments

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.apisov.percusoid.R
import com.apisov.percusoid.data.Instrument
import com.apisov.percusoid.databinding.FragmentInstrumentsBinding
import org.koin.android.architecture.ext.getSharedViewModel

class InstrumentsFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentInstrumentsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = DataBindingUtil.inflate<FragmentInstrumentsBinding>(
            inflater,
            R.layout.fragment_instruments,
            container,
            false
        ).apply {
            val instrumentsViewModel = this@InstrumentsFragment.getSharedViewModel<InstrumentsViewModel>()
            viewModel = instrumentsViewModel
            instrumentsList.adapter = InstrumentsAdapter(object : InstrumentsAdapter.OnInstrumentClickListener{
                override fun onInstrumentClicked(instrument: Instrument) {
                    instrumentsViewModel.openInstrumentEvent.value = instrument.id
                }
            })
            instrumentsViewModel.start()
        }

        return viewDataBinding.root
    }

    companion object {
        fun newInstance() = InstrumentsFragment()
    }
}