package com.krendel.neusfeet.utils

import androidx.paging.PageKeyedDataSource

open class SimplePagedLoadCallback<Key, Value> : PageKeyedDataSource.LoadCallback<Key, Value>() {
    override fun onResult(data: MutableList<Value>, adjacentPageKey: Key?) {

    }
}