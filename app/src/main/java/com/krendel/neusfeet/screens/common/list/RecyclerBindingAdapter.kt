package com.krendel.neusfeet.screens.common.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.subjects.Subject

class RecyclerBindingAdapter(
    items: List<ListItemViewMvc<*>>? = null,
    private val eventsObserver: Subject<ListItemActions>
) : RecyclerView.Adapter<ViewHolderBinding<*>>() {

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    init {
        differ.submitList(items)
    }

    override fun getItemViewType(position: Int): Int = differ.currentList[position].viewType

    override fun getItemCount(): Int = differ.currentList.size

    override fun getItemId(position: Int): Long = differ.currentList[position].id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBinding<*> {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, viewType, parent, false)
        return ViewHolderBinding(binding, eventsObserver)
    }

    override fun onBindViewHolder(holder: ViewHolderBinding<*>, position: Int) =
        holder.bind(differ.currentList[position])

    override fun onViewRecycled(holder: ViewHolderBinding<*>) = holder.unbind()

    fun setItems(items: List<ListItemViewMvc<*>>?) {
        differ.submitList(items)
    }

    companion object {
        private val DIFF_CALLBACK = DiffCallback()
    }
}