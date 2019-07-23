package com.krendel.neusfeet.screens.bookmarks.data

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.repository.bookmark.BookmarksRepository
import com.krendel.neusfeet.screens.common.repository.bookmark.RemoveBookmarkUseCase
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.Completable

class BookmarksDataInteractorImpl(
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
    private val repository: BookmarksRepository
) : BookmarksDataInteractor {

    override fun removeBookmark(article: Article): Completable =
        removeBookmarkUseCase.remove(article)

    override fun bookmarks(pageSize: Int): PagedListing<ArticleItemViewModel> =
        repository.bookmarks(pageSize)

}