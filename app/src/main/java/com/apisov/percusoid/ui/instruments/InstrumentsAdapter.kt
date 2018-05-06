package com.apisov.percusoid.ui.instruments

import android.view.LayoutInflater
import android.view.ViewGroup
import com.apisov.percusoid.data.Instrument
import com.apisov.percusoid.databinding.InstrumentItemBinding
import com.apisov.percusoid.ui.common.DataBoundListAdapter

class InstrumentsAdapter(private val onInstrumentClickListener: OnInstrumentClickListener) :
    DataBoundListAdapter<Instrument, InstrumentItemBinding>() {

    interface OnInstrumentClickListener {
        fun onInstrumentClicked(instrument: Instrument)
    }

    override fun createBinding(parent: ViewGroup) = InstrumentItemBinding.inflate(
        LayoutInflater.from(parent.context),
        parent,
        false
    )

    override fun bind(binding: InstrumentItemBinding, item: Instrument) {
        binding.root.setOnClickListener({ onInstrumentClickListener.onInstrumentClicked(item) })
        binding.instrument = item
    }

    override fun areItemsTheSame(oldItem: Instrument, newItem: Instrument) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Instrument, newItem: Instrument) = oldItem == newItem
}
