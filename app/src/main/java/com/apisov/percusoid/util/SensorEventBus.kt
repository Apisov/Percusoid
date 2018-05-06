package com.apisov.percusoid.util


import io.reactivex.subjects.PublishSubject

object SensorEventBus {

    val subject: PublishSubject<Float> = PublishSubject.create<Float>()

    fun post(magnitude: Float?) {
        if (subject.hasObservers()) {
            subject.onNext(magnitude!!)
        }
    }
}
