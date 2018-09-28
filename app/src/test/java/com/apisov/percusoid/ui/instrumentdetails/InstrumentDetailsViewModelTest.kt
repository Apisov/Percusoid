package com.apisov.percusoid.ui.instrumentdetails

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Observer
import com.apisov.percusoid.data.Input
import com.apisov.percusoid.data.Instrument
import com.apisov.percusoid.data.source.InputsRepository
import com.apisov.percusoid.data.source.InstrumentsRepository
import com.apisov.percusoid.mock
import com.apisov.percusoid.util.rx.ImmediateSchedulerProvider
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class InstrumentDetailsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val instrumentId = "1"
    private val inputId = "2"

    private val instrument = Instrument(
        instrumentId, "Instrument",
        listOf(Input(inputId, instrumentId, "Input"))
    )

    private val instrumentsRepository = mock(InstrumentsRepository::class.java)
    private val inputsRepository = mock(InputsRepository::class.java)
    private val schedulerProvider = ImmediateSchedulerProvider()

    private var viewModel = InstrumentDetailsViewModel(
        null,
        instrumentsRepository,
        inputsRepository,
        schedulerProvider
    )

    @Test
    fun onDeleteClicked_fireDeleteDialogEvent() {
        val observer = mock<Observer<Unit>>()
        viewModel.deleteDialogEvent.observeForever(observer)

        viewModel.onDeleteClicked()
        verify(observer).onChanged(any())
    }

    @Test
    fun onSaveClicked_fireInstrumentSavedEvent() {
        val observer = mock<Observer<Unit>>()
        viewModel.instrumentSavedEvent.observeForever(observer)
        `when`(instrumentsRepository.saveInstrument(Instrument())).thenReturn(Single.just(""))

        viewModel.onSaveClicked()
        verify(observer).onChanged(any())
    }
}
