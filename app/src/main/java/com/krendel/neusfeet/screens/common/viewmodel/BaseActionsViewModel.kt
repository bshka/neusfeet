package com.krendel.neusfeet.screens.common.viewmodel

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

abstract class BaseActionsViewModel<T : ViewModelActions> : BaseViewModel() {

    /**
     * This subject used for notifying view about View Model events
     * */
    private val eventSubject: PublishSubject<T> = PublishSubject.create()

    /**
     * View (fragment) should subscribe on this  this observable for working with View Model events
     * */
    val eventsObservable: Observable<T> get() = eventSubject

    protected fun sendEvent(event: T) = eventSubject.onNext(event)
}

interface ViewModelActions