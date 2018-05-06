package com.apisov.percusoid.ui.instruments

import android.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.mockito.MockitoAnnotations

class InstrumentsViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupInstrumentsViewModel() {
        MockitoAnnotations.initMocks(this)
    }
}
