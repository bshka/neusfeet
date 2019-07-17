package com.krendel.neusfeet.utils

import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulersProvider : SchedulersProvider {

    private val testScheduler = TestScheduler()

    override fun io(): Scheduler = testScheduler
    override fun computation(): Scheduler = testScheduler
    override fun main(): Scheduler = testScheduler

    fun triggerActions(): Unit = testScheduler.triggerActions()
}