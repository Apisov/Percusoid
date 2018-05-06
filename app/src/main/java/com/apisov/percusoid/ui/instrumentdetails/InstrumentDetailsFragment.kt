package com.apisov.percusoid.ui.instrumentdetails

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.apisov.percusoid.databinding.FragmentInstrumentDetailsBinding
import com.apisov.percusoid.service.AVERAGE_BUFFER
import com.apisov.percusoid.util.SensorEventBus
import com.shawnlin.numberpicker.NumberPicker
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_instrument_details.*
import org.koin.android.architecture.ext.getSharedViewModel


class InstrumentDetailsFragment : Fragment() {
    private lateinit var viewDataBinding: FragmentInstrumentDetailsBinding
    private lateinit var instrumentDetailsViewModel: InstrumentDetailsViewModel
    private lateinit var disposable: Disposable

    private val instrumentNameWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            instrumentDetailsViewModel.setName(s.toString())
        }
    }

    private abstract class SimpleTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentInstrumentDetailsBinding.inflate(
            inflater,
            container,
            false
        ).apply {
            instrumentDetailsViewModel = getSharedViewModel(parameters = {
                mapOf(
                    InstrumentDetailsActivity.EXTRA_INSTRUMENT_ID to activity!!.intent.getStringExtra(
                        InstrumentDetailsActivity.EXTRA_INSTRUMENT_ID
                    )
                )
            })

            instrumentName.addTextChangedListener(instrumentNameWatcher)

            sensorSensitivity.setOnSeekBarChangeListener(object : SimpleSeekBarChangeListener() {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    newProgress: Int,
                    fromUser: Boolean
                ) {
                    val progress = adjustSeekBarProgress(newProgress)
                    graph.sensorMaxValuePercentage = progress
                    if (fromUser) {
                        instrumentDetailsViewModel.onSensorSensitivityChanged(
                            progress
                        )
                        instrumentDetailsViewModel.onInstrumentSensitivityChanged(
                            graph.instrumentMaxValue(), progress
                        )
                        instrumentDetailsViewModel.onInstrumentThresholdChanged(
                            graph.instrumentThreshold(), progress
                        )
                    }
                }
            })

            instrumentThreshold.setOnSeekBarChangeListener(object : SimpleSeekBarChangeListener() {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    newProgress: Int,
                    fromUser: Boolean
                ) {
                    val progress = adjustSeekBarProgress(newProgress)

                    graph.instrumentThresholdPercentage(progress)
                    if (fromUser) {
                        instrumentDetailsViewModel.onInstrumentThresholdChanged(
                            graph.instrumentThreshold(), progress
                        )
                    }
                }
            })

            instrumentSensitivity.setOnSeekBarChangeListener(object :
                SimpleSeekBarChangeListener() {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    newProgress: Int,
                    fromUser: Boolean
                ) {
                    val progress = adjustSeekBarProgress(newProgress)

                    graph.instrumentMaxValuePercentage(progress)
                    if (fromUser) {
                        instrumentDetailsViewModel.onInstrumentSensitivityChanged(
                            graph.instrumentMaxValue(), progress
                        )
                    }
                }
            })

            initMidiConfig(midiNote, { instrumentDetailsViewModel.onNoteChanged(it) })
            initMidiConfig(midiChannel, { instrumentDetailsViewModel.onChannelChanged(it) })
            initMidiConfig(midiControl, { instrumentDetailsViewModel.onControlChanged(it) })

            with(instrumentDetailsViewModel) {
                viewModel = this
                onClearedEvent.observe(
                    this@InstrumentDetailsFragment,
                    Observer {
                        instrumentName.removeTextChangedListener(
                            instrumentNameWatcher
                        )
                    })

                onHitEvent.observe(
                    this@InstrumentDetailsFragment,
                    Observer { graph.addHit(it!!) })
            }

            instrumentDetailsViewModel.start()
        }

        disposable = SensorEventBus.subject
            .buffer(AVERAGE_BUFFER, 1)
            .map { it.average().toFloat() }
            .map { graph.toYPoint(it) }
            .buffer(GRAPH_BUFFER_SIZE, 1)
            .retry()
            .subscribe { magnitude -> graph.addValues(magnitude) }

        return viewDataBinding.root
    }

    private fun adjustSeekBarProgress(progress: Int) = if (progress == 0) 1 else progress

    private fun initMidiConfig(
        numberPicker: NumberPicker,
        action: (value: Int) -> Unit
    ) {
        numberPicker.setOnValueChangedListener { _, _, value -> action.invoke(value) }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    companion object {
        fun newInstance() = InstrumentDetailsFragment()
    }
}

private abstract class SimpleSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {

    override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
    override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
}