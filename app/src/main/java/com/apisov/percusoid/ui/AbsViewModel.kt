package com.apisov.percusoid.ui

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class AbsViewModel(private val disposables: CompositeDisposable = CompositeDisposable()) :
    ViewModel() {

    fun run(job: () -> Disposable) {
        disposables.add(job())
    }

    abstract fun start()

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}