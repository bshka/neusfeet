package com.krendel.neusfeet.screens.common.viewmodel

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class BaseActionsViewModel<T : ViewModelActions> : BaseViewModel() {

    /**
     * This subject used for notifying view about View Model events
     * */
    private val eventSubject: PublishSubject<T> = PublishSubject.create()

    /**
     * View (fragment) should subscribe on this  this observable for working with View Model events
     * */
    val eventsObservable: Observable<T> get() = eventSubject

    /**
     * Clears in [stop]. All data associated subjects must be registered via [registerDataSource].
     * Register data source in [start]
     */
    private val dataDisposable = CompositeDisposable()

    protected fun sendEvent(event: T) = eventSubject.onNext(event)

    override fun stop() {
        super.stop()
        dataDisposable.clear()
    }

    /**
     * Register data source [BehaviorSubject] to data source's lifecycle
     */
    protected fun registerDataSource(observable: BehaviorSubject<out T>) {
        dataDisposable.add(
            observable.registerObserver(eventSubject)
        )
    }

    /**
     * Register some outside source of [ViewModelActions] in [androidx.lifecycle.ViewModel] lifecycle
     */
    protected fun registerActionsSource(observable: Observable<out T>) {
        observable.registerObserver(eventSubject)
            .connectToLifecycle()
    }
}

interface ViewModelActions

fun <T : ViewModelActions> Observable<out T>.registerObserver(eventSubject: Subject<in T>): Disposable {
    return subscribe(
        { eventSubject.onNext(it) },
        { eventSubject.onError(it) },
        { eventSubject.onComplete() },
        { eventSubject.onSubscribe(it) }
    )
}