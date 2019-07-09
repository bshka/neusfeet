package com.krendel.neusfeet.screens.common.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import io.reactivex.subjects.Subject

class RecyclerPagingBindingAdapter(
    items: PagedList<ListItemViewMvc<*>>? = null,
    private val eventsObserver: Subject<ListItemActions>
) : PagedListAdapter<ListItemViewMvc<*>, ViewHolderBinding<*>>(DIFF_CALLBACK) {

    init {
        submitList(items)
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  RecyclerView.Adapter methods
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBinding<*> {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)
        return ViewHolderBinding(binding, eventsObserver)
    }

    override fun onBindViewHolder(holder: ViewHolderBinding<*>, position: Int) =
        holder.bind(getItem(position))

    override fun onViewRecycled(holder: ViewHolderBinding<*>) = holder.unbind()

    override fun getItemViewType(position: Int): Int = getItem(position)!!.viewType

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *\
     *  Diff util
    \* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItemViewMvc<*>>() {
            override fun areItemsTheSame(oldItem: ListItemViewMvc<*>, newItem: ListItemViewMvc<*>): Boolean =
                oldItem.isTheSameItem(newItem)

            override fun areContentsTheSame(oldItem: ListItemViewMvc<*>, newItem: ListItemViewMvc<*>): Boolean =
                oldItem.hasTheSameContent(newItem)
        }
    }
}
