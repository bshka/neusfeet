package com.krendel.neusfeet.screens.common.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.krendel.neusfeet.screens.common.list.ListItemViewMvc
import com.krendel.neusfeet.screens.common.list.RecyclerBindingAdapter

@BindingAdapter(value = ["viewModelItems", "viewModelItemsForceUpdate"], requireAll = false)
fun <T : ListItemViewMvc> setupRecyclerViewViewModelItems(
        recycler: RecyclerView,
        items: List<T>?,
        forceUpdate: Boolean?
) {
    (recycler.adapter as? RecyclerBindingAdapter)?.setItems(
            items ?: emptyList(), forceUpdate
            ?: false
    )
}