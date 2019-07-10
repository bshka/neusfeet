package com.krendel.neusfeet.screens.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewSearchBinding
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions

class SearchViewMvc(
    lifecycleOwner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseLifecycleViewMvc<ViewSearchBinding, SearchViewActions>(lifecycleOwner, inflater, container) {

    override val layout: Int = R.layout.view_search

    override fun bindViewModel(dataBinding: ViewSearchBinding) {
        dataBinding.viewModel = this
    }
}

sealed class SearchViewActions : ViewMvcActions