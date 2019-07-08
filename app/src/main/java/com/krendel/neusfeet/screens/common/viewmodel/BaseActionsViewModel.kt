package com.krendel.neusfeet.screens.common.viewmodel

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
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

    private val dataDisposable = CompositeDisposable()

    protected fun sendEvent(event: T) = eventSubject.onNext(event)

    override fun stop() {
        super.stop()
        dataDisposable.clear()
    }

    fun registerDataSource(observable: BehaviorSubject<T>) {
        dataDisposable.add(
            observable.registerPublisher(eventSubject)
        )
    }
}

interface ViewModelActions

fun <T : ViewModelActions> Observable<T>.registerPublisher(eventSubject: PublishSubject<T>): Disposable {
    return subscribe(
        { eventSubject.onNext(it) },
        { eventSubject.onError(it) },
        { eventSubject.onComplete() },
        { eventSubject.onSubscribe(it) }
    )
}