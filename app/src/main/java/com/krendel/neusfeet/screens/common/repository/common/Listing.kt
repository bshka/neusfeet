package com.krendel.neusfeet.screens.common.repository.common

import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.Subject

data class Listing<T : ListItemViewMvc<*>>(
    val dataList: Observable<List<T>>,
    val dataSourceActions: Observable<out DataSourceActions>,
    val refresh: Listener
)
