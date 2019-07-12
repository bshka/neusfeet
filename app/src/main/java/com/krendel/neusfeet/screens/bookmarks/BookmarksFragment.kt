package com.krendel.neusfeet.screens.bookmarks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.screens.common.BaseFragment
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BookmarksFragment : BaseFragment<BookmarksFragmentViewModel, BookmarksViewMvc>() {

    override val viewModel: BookmarksFragmentViewModel by viewModel()

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        lifecycleOwner: LifecycleOwner
    ): BookmarksViewMvc = get { parametersOf(inflater, container, lifecycleOwner) }

    override fun subscribeToViewModel(viewModel: BookmarksFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            // TODO observe view model actions
            when (it) {

            }
        }
    }

    override fun subscribeToView(viewMvc: BookmarksViewMvc) {
        observe(viewMvc.eventsObservable) {
            // TODO observe view mvc actions
            when (it) {

            }
        }
    }
}