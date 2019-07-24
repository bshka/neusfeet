package com.krendel.neusfeet.utils

import androidx.paging.PageKeyedDataSource

open class SimplePagedLoadInitialCallback<Key, Value> : PageKeyedDataSource.LoadInitialCallback<Key, Value>() {
    override fun onResult(
        data: MutableList<Value>,
        position: Int,
        totalCount: Int,
        previousPageKey: Key?,
        nextPageKey: Key?
    ) {

    }

    override fun onResult(data: MutableList<Value>, previousPageKey: Key?, nextPageKey: Key?) {

    }
}