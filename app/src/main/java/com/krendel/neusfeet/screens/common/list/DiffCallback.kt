package com.krendel.neusfeet.screens.common.list

import androidx.recyclerview.widget.DiffUtil

class DiffCallback : DiffUtil.ItemCallback<ListItemViewMvc<*>>() {
    override fun areItemsTheSame(oldItem: ListItemViewMvc<*>, newItem: ListItemViewMvc<*>): Boolean =
        oldItem.isTheSameItem(newItem)

    override fun areContentsTheSame(oldItem: ListItemViewMvc<*>, newItem: ListItemViewMvc<*>): Boolean =
        oldItem.hasTheSameContent(newItem)
}
