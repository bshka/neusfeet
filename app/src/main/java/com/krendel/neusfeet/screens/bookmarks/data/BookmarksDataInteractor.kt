package com.krendel.neusfeet.screens.bookmarks.data

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.Completable

interface BookmarksDataInteractor {
    fun removeBookmark(article: Article): Completable
    fun bookmarks(pageSize: Int): PagedListing<ArticleItemViewModel>
}