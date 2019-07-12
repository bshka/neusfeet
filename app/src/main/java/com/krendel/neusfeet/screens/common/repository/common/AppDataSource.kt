package com.krendel.neusfeet.screens.common.repository.common

import androidx.annotation.MainThread
import androidx.paging.PageKeyedDataSource
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

@MainThread
abstract class AppDataSource<Key, Value>(
    private val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Key, Value>() {

    private val eventSubject: PublishSubject<DataSourceActions> = PublishSubject.create()
    val eventsObservable: Observable<DataSourceActions>
        get() = eventSubject

    final override fun loadInitial(params: LoadInitialParams<Key>, callback: LoadInitialCallback<Key, Value>) {
        startLoading(true)
        compositeDisposable.add(
            loadInitial(params, SourceInitialCallback(callback, eventSubject))
        )
    }

    final override fun loadAfter(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        startLoading(false)
        compositeDisposable.add(
            loadAfter(params, SourceLoadCallback(callback, eventSubject))
        )
    }

    final override fun loadBefore(params: LoadParams<Key>, callback: LoadCallback<Key, Value>) {
        startLoading(false)
        compositeDisposable.add(
            loadBefore(params, SourceLoadCallback(callback, eventSubject))
        )
    }

    /**
     * Load initial with custom callback
     *
     * @return [Disposable] to handle data loading with client lifecycle
     */
    abstract fun loadInitial(params: LoadInitialParams<Key>, callback: SourceInitialCallback<Key, Value>): Disposable

    /**
     * Load after with custom callback
     *
     * @return [Disposable] to handle data loading with client lifecycle
     */
    abstract fun loadAfter(params: LoadParams<Key>, callback: SourceLoadCallback<Key, Value>): Disposable

    /**
     * Load before with custom callback
     *
     * @return [Disposable] to handle data loading with client lifecycle
     */
    abstract fun loadBefore(params: LoadParams<Key>, callback: SourceLoadCallback<Key, Value>): Disposable

    private fun startLoading(isInitial: Boolean) = eventSubject.onNext(
        DataSourceActions.Loading(
            active = true,
            isInitial = isInitial
        )
    )
}

fun <T : DataSourceActions> Observable<out T>.registerObserver(eventSubject: Subject<T>): Disposable {
    return subscribe(
        { eventSubject.onNext(it) },
        { eventSubject.onError(it) },
        { eventSubject.onComplete() },
        { eventSubject.onSubscribe(it) }
    )
}