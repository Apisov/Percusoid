package com.apisov.percusoid.util.ext

import com.apisov.percusoid.util.rx.SchedulerProvider
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Use SchedulerProvider configuration for Single
 */
fun <T> Single<T>.with(schedulerProvider: SchedulerProvider): Single<T> =
    subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())

/**
 * Use SchedulerProvider configuration for Flowable
 */
fun <T> Flowable<T>.with(schedulerProvider: SchedulerProvider): Flowable<T> =
    subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
/**
 * Use SchedulerProvider configuration for Flowable
 */
fun <T> Observable<T>.with(schedulerProvider: SchedulerProvider): Observable<T> =
    subscribeOn(schedulerProvider.io()).observeOn(schedulerProvider.ui())
