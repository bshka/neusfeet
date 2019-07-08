package com.krendel.neusfeet.screens.common.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Alexey Zubkovskiy on 20 May 2019
 */
open class RecyclerBindingAdapter(items: List<ListItemViewMvc> = listOf(), val spanCount: Int = 12) :
    RecyclerView.Adapter<ViewHolderBinding<*>>() {

    private val items = items.toMutableList()

    val spanSizeLookUp: GridLayoutManager.SpanSizeLookup = SpanSizeLookUp(spanCount)

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  RecyclerView.Adapter methods
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    override fun getItemViewType(position: Int): Int = items[position].viewType

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = items[position].id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBinding<*> {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)
        return ViewHolderBinding(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderBinding<*>, position: Int) = holder.bind(items[position])

    override fun onViewRecycled(holder: ViewHolderBinding<*>) = holder.unbind()

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  Setup helper method
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Helper method to setup a [RecyclerView] with this adapter and a GridLayoutManager
     *
     * @param vertical if false, it will set the orientation as horizontal
     */
    fun setupRecyclerView(recyclerView: RecyclerView, vertical: Boolean = true, reverseLayout: Boolean = false) {
        val recyclerAdapter = this
        val orientation = if (vertical) GridLayoutManager.VERTICAL else GridLayoutManager.HORIZONTAL
        recyclerView.run {
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(context, recyclerAdapter.spanCount, orientation, reverseLayout).apply {
                spanSizeLookup = recyclerAdapter.spanSizeLookUp
            }
        }
    }

    fun setupRecyclerView(recyclerView: RecyclerView, manager: RecyclerView.LayoutManager?) {
        if (manager == null) {
            setupRecyclerView(recyclerView)
            return
        }

        val recyclerAdapter = this
        recyclerView.run {
            adapter = recyclerAdapter
            layoutManager = manager
            notifyDataSetChanged()
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  Item CRUD methods
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    fun getItem(position: Int): ListItemViewMvc = items[position]

    fun getItems(): List<ListItemViewMvc> = items.toList()

    fun setItems(vararg newItems: ListItemViewMvc) = setItems(newItems.asList())

    open fun setItems(newItems: List<ListItemViewMvc>) {
        val diffResult = DiffUtil.calculateDiff(DiffHelper(newItems, items))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setItems(newItems: List<ListItemViewMvc>, forced: Boolean = false) {
        if (forced) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        } else {
            setItems(newItems)
        }
    }

    /**
     * Add item to the list, by default - to the end of the list
     */
    fun addItem(newItem: ListItemViewMvc, index: Int = items.lastIndex) {
        items.add(index, newItem)
        notifyItemInserted(index)
    }

    /**
     * Replace item in the list
     */
    fun replaceItemAt(position: Int, viewModel: ListItemViewMvc) = setItems(items.mapIndexed { index, itemViewModel ->
        when (index) {
            position -> viewModel
            else -> itemViewModel
        }
    })

    /**
     * Remove item from the list
     */
    fun removeItem(item: ListItemViewMvc) = setItems(items - item)

    /**
     * Remove item at position from the list
     */
    fun removeItemAt(position: Int) = setItems(items.filterIndexed { index, _ -> index != position })

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  Span Size Look Up - For GridLayoutManager
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private inner class SpanSizeLookUp(val spanCount: Int) : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int = getItem(position).getSpanSize(spanCount)
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  Diff util
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private class DiffHelper(val newItems: List<ListItemViewMvc>, val oldItems: List<ListItemViewMvc>) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size
        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].isTheSameItem(newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].hasTheSameContent(newItems[newItemPosition])
    }
}
/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
 *  Helper for data binding
\* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

data class RecyclerSetupDetails(
    val adapter: RecyclerBindingAdapter,
    val vertical: Boolean = true,
    val reverseLayout: Boolean = false,
    val layoutManager: RecyclerView.LayoutManager? = null
)