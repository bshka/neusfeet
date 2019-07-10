package com.krendel.neusfeet.screens.common.repository.common

import androidx.annotation.MainThread
import androidx.paging.PageKeyedDataSource
import io.reactivex.subjects.PublishSubject

/**
 * Wrapper for initial callback with onError method, so we can notify UI about errors
 */
@MainThread
class SourceInitialCallback<Key, Value>(
    private val callback: PageKeyedDataSource.LoadInitialCallback<Key, Value>,
    private val eventSubject: PublishSubject<DataSourceActions>
) : PageKeyedDataSource.LoadInitialCallback<Key, Value>() {

    override fun onResult(data: List<Value>, position: Int, totalCount: Int, previousPageKey: Key?, nextPageKey: Key?) {
        callback.onResult(data, position, totalCount, previousPageKey, nextPageKey)
        stopLoading()
    }

    override fun onResult(data: List<Value>, previousPageKey: Key?, nextPageKey: Key?) {
        callback.onResult(data, previousPageKey, nextPageKey)
        stopLoading()
    }

    fun onError(throwable: Throwable) {
        error(throwable)
        stopLoading()
    }

    private fun stopLoading() = eventSubject.onNext(
        DataSourceActions.Loading(
            active = false
        )
    )

    private fun error(throwable: Throwable) = eventSubject.onNext(
        DataSourceActions.Error(
            throwable = throwable
        )
    )

}