package com.krendel.neusfeet.screens.bookmarks

import androidx.paging.PagedList
import com.krendel.neusfeet.local.bookmarks.BookmarksDao
import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.bookmark.RemoveBookmarkUseCase
import com.krendel.neusfeet.screens.common.viewmodel.BaseActionsViewModel
import com.krendel.neusfeet.screens.common.viewmodel.ViewModelActions
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber

class BookmarksFragmentViewModel(
    bookmarksDao: BookmarksDao,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
    private val schedulersProvider: SchedulersProvider
) : BaseActionsViewModel<BookmarksViewModelActions>() {

    private val bookmarksData: BehaviorSubject<BookmarksViewModelActions.BookmarksLoaded> = BehaviorSubject.create()

    init {

//        bookmarksDao.all()
//            .map { it.map { article -> ArticleItemViewModel(article) } }
//            .map { BookmarksViewModelActions.BookmarksLoaded(it) }
//            .subscribe(
//                { sendEvent(it) },
//                { Timber.e(it) }
//            ).connectToLifecycle()
    }

    override fun start() {
        super.start()
        registerDataSource(bookmarksData)
    }

    fun removeBookmark(article: Article) {
        removeBookmarkUseCase.remove(article)
            .observeOn(schedulersProvider.main())
            .subscribe(
                { sendEvent(BookmarksViewModelActions.BookmarkRemoved) },
                { Timber.e(it) }
            ).connectToLifecycle()
    }

}

sealed class BookmarksViewModelActions : ViewModelActions {
    object BookmarkRemoved : BookmarksViewModelActions()
    data class BookmarksLoaded(val list: PagedList<ArticleItemViewModel>) : BookmarksViewModelActions()
}