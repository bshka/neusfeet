package com.krendel.neusfeet.screens.bookmarks

import androidx.navigation.fragment.findNavController
import com.krendel.neusfeet.screens.common.BaseFragment
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class BookmarksFragment : BaseFragment<BookmarksFragmentViewModel, BookmarksViewMvc>() {

    override val viewModel: BookmarksFragmentViewModel by viewModel()

    override fun createView(configuration: LifecycleViewMvcConfiguration): BookmarksViewMvc =
        get { parametersOf(configuration) }

    override fun subscribeToViewModel(viewModel: BookmarksFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            when (it) {
                is BookmarksViewModelActions.BookmarksLoaded -> viewMvc.setBookmarks(it.list)
                is BookmarksViewModelActions.BookmarkRemoved -> viewMvc.bookmarkRemoved()
            }
        }
    }

    override fun subscribeToView(viewMvc: BookmarksViewMvc) {
        observe(viewMvc.eventsObservable) {
            when (it) {
                is BookmarksViewActions.ArticleClicked -> {
                    findNavController().navigate(
                        BookmarksFragmentDirections.actionBookmarksToPreview(
                            it.article,
                            false
                        )
                    )
                }
                is BookmarksViewActions.RemoveBookmark -> viewModel.removeBookmark(it.article)
            }
        }
    }
}