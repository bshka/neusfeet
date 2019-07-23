package com.krendel.neusfeet.screens.search.data

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.repository.RepositoryFactory
import com.krendel.neusfeet.screens.common.repository.bookmark.AddBookmarkUseCase
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.repository.everything.EverythingFetchConfiguration
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class SearchDataInteractorImpl(
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val repositoryFactory: RepositoryFactory
) : SearchDataInteractor {

    override fun addBookmark(article: Article): Completable =
        addBookmarkUseCase.add(article)

    override fun everything(
        configuration: EverythingFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ): PagedListing<ArticleItemViewModel> =
        repositoryFactory.everythingRepository(configuration, compositeDisposable).everything(configuration.pageSize)
}