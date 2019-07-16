package com.krendel.neusfeet.screens.preview

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.bookmark.AddBookmarkUseCase
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions
import timber.log.Timber

class PreviewFragmentViewModel(
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val schedulersProvider: SchedulersProvider
) : BaseActionsViewModel<PreviewViewModelActions>() {

    fun addBookmark(article: Article) {
        addBookmarkUseCase.add(article)
            .observeOn(schedulersProvider.main())
            .subscribe(
                { sendEvent(PreviewViewModelActions.BookmarkAdded) },
                { Timber.e(it) }
            ).connectToLifecycle()
    }

}

sealed class PreviewViewModelActions : ViewModelActions {
    object BookmarkAdded : PreviewViewModelActions()
}