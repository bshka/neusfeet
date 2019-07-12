package com.krendel.neusfeet.screens.bookmarks

import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewBookmarksBinding
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.ViewMvcActions

class BookmarksViewMvc(
    configuration: LifecycleViewMvcConfiguration
) : BaseLifecycleViewMvc<LifecycleViewMvcConfiguration, ViewBookmarksBinding, BookmarksViewActions>(configuration) {

    override val layout: Int = R.layout.view_bookmarks

    override fun bindViewModel(dataBinding: ViewBookmarksBinding) {
        dataBinding.viewModel = this
    }
}

sealed class BookmarksViewActions : ViewMvcActions