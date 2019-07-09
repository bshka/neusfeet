package com.krendel.neusfeet.screens.common.list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.krendel.neusfeet.BR
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.Subject

class ViewHolderBinding<in T : ViewDataBinding>(
    private val binding: T,
    private val eventsObserver: Subject<ListItemActions>
) : RecyclerView.ViewHolder(binding.root) {
    var viewModel: ListItemViewMvc<*>? = null
        private set
    private var reused = false

    private var disposable: Disposable? = null

    fun bind(viewModel: ListItemViewMvc<*>?) {
        if (viewModel == null) {
            return
        }
        this.viewModel = viewModel
        disposable = viewModel.eventsObservable.registerObserver(eventsObserver)

        if (!reused) {
            viewModel.onSetup(binding)
        }

        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
        viewModel.onBind(binding)

        reused = true
    }

    fun unbind() {
        disposable?.dispose()
        binding.setVariable(BR.viewModel, null)
        binding.executePendingBindings()
        viewModel?.onUnbind(binding)
        viewModel = null
    }
}