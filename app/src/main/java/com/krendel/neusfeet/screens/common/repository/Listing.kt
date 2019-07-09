package com.krendel.neusfeet.screens.common.repository

import androidx.paging.PagedList
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions
import io.reactivex.Observable

data class Listing<T : ListItemViewMvc<*>>(
    val dataList: Observable<PagedList<T>>,
    val refresh: Listener,
    val error: Listener
)
