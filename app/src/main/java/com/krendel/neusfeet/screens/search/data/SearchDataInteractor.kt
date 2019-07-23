package com.krendel.neusfeet.screens.search.data

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.repository.everything.EverythingFetchConfiguration
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

interface SearchDataInteractor {

    fun addBookmark(article: Article): Completable

    fun everything(
        configuration: EverythingFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ): PagedListing<ArticleItemViewModel>

}