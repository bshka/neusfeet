package com.krendel.neusfeet.screens.home.data

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.screens.common.repository.RepositoryFactory
import com.krendel.neusfeet.screens.common.repository.bookmark.AddBookmarkUseCase
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesFetchConfiguration
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable

class HomeDataInteractorImpl(
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val repositoryFactory: RepositoryFactory
) : HomeDataInteractor {

    override fun addBookmark(article: Article): Completable =
        addBookmarkUseCase.add(article)

    override fun headlinesListing(
        configuration: TopHeadlinesFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ): PagedListing<ArticleItemViewModel> =
        repositoryFactory.topHeadlinesRepository(configuration, compositeDisposable)
            .headlines(configuration.pageSize)
}