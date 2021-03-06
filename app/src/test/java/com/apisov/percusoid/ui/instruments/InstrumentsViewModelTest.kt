package com.apisov.percusoid.ui.instruments

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.apisov.percusoid.data.Instrument
import com.apisov.percusoid.data.source.InstrumentsRepository
import com.apisov.percusoid.util.rx.ImmediateSchedulerProvider
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*
import kotlin.test.assertEquals

@RunWith(JUnit4::class)
class InstrumentsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val instruments = listOf(Instrument(), Instrument(), Instrument())

    private val repository = mock(InstrumentsRepository::class.java)
    private val viewModel = InstrumentsViewModel(repository, ImmediateSchedulerProvider())

    @Test
    fun testNull() {
        assertThat(viewModel.instruments, notNullValue())
        verify(repository, never()).getInstruments()
    }

    @Test
    fun loadNonEmptyInstruments() {
        `when`(repository.getInstruments()).thenReturn(Flowable.just(instruments))
        viewModel.start()

        verify(repository, times(1)).getInstruments()
        assertEquals(instruments, viewModel.instruments)
    }

    @Test
    fun loadInstrumentsAndUpdate() {
        var emitter: FlowableEmitter<List<Instrument>>? = null
        `when`(repository.getInstruments())
            .thenReturn(Flowable.create({
                emitter = it
                it.onNext(instruments)
            }, BackpressureStrategy.DROP))

        viewModel.start()

        val newInstruments = listOf(Instrument())
        emitter?.onNext(newInstruments)
        assertEquals(newInstruments, viewModel.instruments)
    }
}
