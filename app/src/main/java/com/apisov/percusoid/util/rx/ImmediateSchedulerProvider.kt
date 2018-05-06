package com.apisov.percusoid.util.rx

import io.reactivex.schedulers.Schedulers

class ImmediateSchedulerProvider : SchedulerProvider {

    override fun io() = Schedulers.trampoline()

    override fun ui() = Schedulers.trampoline()

    override fun computation() = Schedulers.trampoline()
}