package com.krendel.neusfeet.screens.common.list

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.krendel.neusfeet.BR

class ViewHolderBinding<out T : ViewDataBinding>(private val binding: T) : RecyclerView.ViewHolder(binding.root) {
    var viewModel: ItemViewModel? = null
        private set
    private var reused = false

    fun bind(viewModel: ItemViewModel) {
        this.viewModel = viewModel

        if (!reused) viewModel.onSetup(binding)

        binding.setVariable(BR.viewModel, viewModel)
        binding.executePendingBindings()
        viewModel.onBind(binding)

        reused = true
    }

    fun unbind() {
        binding.setVariable(BR.viewModel, null)
        binding.executePendingBindings()
        viewModel?.onUnbind(binding)
        viewModel = null
    }
}