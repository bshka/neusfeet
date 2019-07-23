package com.krendel.neusfeet.screens.home.data

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesFetchConfiguration
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

interface HomeDataInteractor {
    fun addBookmark(article: Article): Completable
    fun headlinesListing(
        configuration: TopHeadlinesFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ): PagedListing<ArticleItemViewModel>
}