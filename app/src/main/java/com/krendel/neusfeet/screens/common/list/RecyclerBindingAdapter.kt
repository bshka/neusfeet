package com.krendel.neusfeet.screens.common.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Alexey Zubkovskiy on 20 May 2019
 */
class RecyclerBindingAdapter(items: List<ListItemViewMvc> = listOf(), val spanCount: Int = 12) :
    RecyclerView.Adapter<ViewHolderBinding<*>>() {

    private val listDiffer = AsyncListDiffer(this, DIFF_CALLBACK)

    init {
        listDiffer.submitList(items)
    }

    private val spanSizeLookUp: GridLayoutManager.SpanSizeLookup = SpanSizeLookUp(spanCount)

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  RecyclerView.Adapter methods
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    override fun getItemViewType(position: Int): Int = listDiffer.currentList[position].viewType

    override fun getItemCount(): Int = listDiffer.currentList.size

    override fun getItemId(position: Int): Long = listDiffer.currentList[position].id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBinding<*> {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)
        return ViewHolderBinding(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderBinding<*>, position: Int) =
        holder.bind(listDiffer.currentList[position])

    override fun onViewRecycled(holder: ViewHolderBinding<*>) = holder.unbind()

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  Setup helper method
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Helper method to setup a [RecyclerView] with this adapter and a GridLayoutManager
     *
     * @param vertical if false, it will set the orientation as horizontal
     */
    @SuppressLint("WrongConstant")
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

    fun getItem(position: Int): ListItemViewMvc = listDiffer.currentList[position]

    fun getItems(): List<ListItemViewMvc> = listDiffer.currentList

    fun setItems(vararg newItems: ListItemViewMvc) = setItems(newItems.asList())

    open fun setItems(newItems: List<ListItemViewMvc>) {
        listDiffer.submitList(newItems)
    }

    fun setItems(newItems: List<ListItemViewMvc>, forced: Boolean = false) {
//        if (forced) {
//            items.clear()
//            items.addAll(newItems)
//            notifyDataSetChanged()
//        } else {
        setItems(newItems)
//        }
    }

    /**
     * Add item to the list, by default - to the end of the list
     */
    fun addItem(newItem: ListItemViewMvc, index: Int = listDiffer.currentList.lastIndex) {
        val items = listDiffer.currentList.toMutableList()
        items.add(index, newItem)
        listDiffer.submitList(items)
    }

    /**
     * Replace item in the list
     */
    fun replaceItemAt(position: Int, viewModel: ListItemViewMvc) =
        setItems(listDiffer.currentList.mapIndexed { index, itemViewModel ->
            when (index) {
                position -> viewModel
                else -> itemViewModel
            }
        })

    /**
     * Remove item from the list
     */
    fun removeItem(item: ListItemViewMvc) = setItems(listDiffer.currentList - item)

    /**
     * Remove item at position from the list
     */
    fun removeItemAt(position: Int) = setItems(listDiffer.currentList.filterIndexed { index, _ -> index != position })

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  Span Size Look Up - For GridLayoutManager
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    private inner class SpanSizeLookUp(val spanCount: Int) : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int = getItem(position).getSpanSize(spanCount)
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  Diff util
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
    
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItemViewMvc>() {
            override fun areItemsTheSame(oldItem: ListItemViewMvc, newItem: ListItemViewMvc): Boolean =
                oldItem.isTheSameItem(newItem)

            override fun areContentsTheSame(oldItem: ListItemViewMvc, newItem: ListItemViewMvc): Boolean =
                oldItem.hasTheSameContent(newItem)
        }
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