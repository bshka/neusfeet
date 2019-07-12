package com.krendel.neusfeet.screens.bookmarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewBookmarksBinding
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions

class BookmarksViewMvc(
    inflater: LayoutInflater,
    container: ViewGroup?,
    lifecycleOwner: LifecycleOwner
): BaseLifecycleViewMvc<ViewBookmarksBinding, BookmarksViewActions>(inflater, container, lifecycleOwner) {

    override val layout: Int = R.layout.view_bookmarks

    override fun bindViewModel(dataBinding: ViewBookmarksBinding) {
        dataBinding.viewModel = this
    }
}

sealed class BookmarksViewActions: ViewMvcActions