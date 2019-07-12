package com.krendel.neusfeet.screens.common.repository.common

import androidx.annotation.MainThread
import androidx.paging.PageKeyedDataSource
import io.reactivex.subjects.PublishSubject

/**
 * Wrapper for load callback with onError method, so we can notify UI about errors
 */
@MainThread
class SourceLoadCallback<Key, Value>(
    private val callback: PageKeyedDataSource.LoadCallback<Key, Value>,
    private val eventSubject: PublishSubject<DataSourceActions>
) : PageKeyedDataSource.LoadCallback<Key, Value>() {

    override fun onResult(data: List<Value>, adjacentPageKey: Key?) {
        stopLoading()
        callback.onResult(data, adjacentPageKey)
    }

    fun onError(throwable: Throwable) {
        stopLoading()
        error(throwable)
    }

    private fun stopLoading() = eventSubject.onNext(
        DataSourceActions.Loading(
            active = false,
            isInitial = false
        )
    )

    private fun error(throwable: Throwable) = eventSubject.onNext(
        DataSourceActions.Error(
            throwable = throwable
        )
    )

}