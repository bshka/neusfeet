package com.krendel.neusfeet.screens.common.views.articles

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ArticlesListTouchHelper(
    var swipeEnabled: Boolean = false,
    private val itemSwipedAction: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START.or(ItemTouchHelper.END)) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        itemSwipedAction(viewHolder.adapterPosition)
    }

    override fun isItemViewSwipeEnabled(): Boolean = swipeEnabled
}