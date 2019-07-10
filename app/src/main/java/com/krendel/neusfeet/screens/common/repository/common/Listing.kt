package com.krendel.neusfeet.screens.common.repository.common

import androidx.paging.PagedList
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc
import io.reactivex.Observable

data class Listing<T : ListItemViewMvc<*>>(
    val dataList: Observable<PagedList<T>>,
    val dataSourceActions: Observable<out DataSourceActions>,
    val refresh: Listener
)
