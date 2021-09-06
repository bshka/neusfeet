package com.krendel.neusfeet.data.networking.schedulers

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun main(): Scheduler
}