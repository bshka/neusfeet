package com.krendel.neusfeet.screens.common.binding

import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc
import com.krendel.neusfeet.screens.common.list.RecyclerPagingBindingAdapter

@BindingAdapter("viewModelItems")
fun <T : ListItemViewMvc<*>> setupRecyclerViewViewModelItems(recycler: RecyclerView, items: PagedList<T>?) {
    (recycler.adapter as? RecyclerPagingBindingAdapter)?.submitList(items as PagedList<ListItemViewMvc<*>>?)
}