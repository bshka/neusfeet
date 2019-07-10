package com.krendel.neusfeet.screens.common.repository.common

import androidx.paging.DataSource
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class AppDataSourceFactory<SourceType : AppDataSource<Int, Value>, Value>
    : DataSource.Factory<Int, Value>() {

    /**
     * Data source holder
     */
    protected abstract val dataSource: BehaviorSubject<SourceType>

    /**
     * Subject that mediates actions between data source and receiver. Has different lifecycle than
     * data source.
     */
    private val dataSourceEventsObservable: PublishSubject<DataSourceActions> = PublishSubject.create()

    /**
     * Link to [com.krendel.neusfeet.screens.common.repository.AppDataSource.eventsObservable] disposable.
     * Need to dispose of because new data source will be created after invalidation
     */
    private var dataSourceEventsDisposable: Disposable? = null


    final override fun create(): SourceType {
        val source = newSource()

        dataSourceEventsDisposable = source.eventsObservable
            .registerObserver(dataSourceEventsObservable)

        dataSource.onNext(source)
        source.addInvalidatedCallback(
            DataSourceInvalidatedCallback(
                source
            ) {
                dataSourceEventsDisposable?.dispose()
            })

        return source
    }

    protected abstract fun newSource(): SourceType

    /**
     * Refresh data in data source. Calls data source's [DataSource.invalidate] method
     */
    fun refresh() = dataSource.value?.invalidate()

    /**
     * Data source events observable
     */
    fun eventsObservable(): Observable<DataSourceActions> = dataSourceEventsObservable

}

/**
 * Helper class that helps with disposing of events publisher
 */
private class DataSourceInvalidatedCallback<Key, Value>(
    private val source: DataSource<Key, Value>,
    private val invalidated: () -> Unit
) : DataSource.InvalidatedCallback {

    init {
        source.addInvalidatedCallback(this)
    }

    override fun onInvalidated() {
        source.removeInvalidatedCallback(this)
        invalidated()
    }
}