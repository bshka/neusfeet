package com.krendel.neusfeet.screens.common.repository

import com.krendel.neusfeet.local.bookmarks.BookmarksDao
import com.krendel.neusfeet.local.source.SourceDao
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.bookmark.BookmarksRepositoryImpl
import com.krendel.neusfeet.screens.common.repository.everything.EverythingFetchConfiguration
import com.krendel.neusfeet.screens.common.repository.everything.EverythingRepository
import com.krendel.neusfeet.screens.common.repository.sources.SourcesFetchConfiguration
import com.krendel.neusfeet.screens.common.repository.sources.SourcesRepositoryImpl
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesFetchConfiguration
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesRepository
import io.reactivex.disposables.CompositeDisposable

class RepositoryFactoryImpl(
    private val bookmarksDao: BookmarksDao,
    private val sourceDao: SourceDao,
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider
): RepositoryFactory {

    override fun bookmarksRepository() =
        BookmarksRepositoryImpl(
            bookmarksDao = bookmarksDao,
            schedulersProvider = schedulersProvider
        )

    override fun topHeadlinesRepository(
        configuration: TopHeadlinesFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ) =
        TopHeadlinesRepository(
            sourceDao = sourceDao,
            newsApi = newsApi,
            schedulersProvider = schedulersProvider,
            compositeDisposable = compositeDisposable,
            configuration = configuration
        )

    override fun everythingRepository(configuration: EverythingFetchConfiguration, compositeDisposable: CompositeDisposable) =
        EverythingRepository(
            newsApi = newsApi,
            schedulersProvider = schedulersProvider,
            compositeDisposable = compositeDisposable,
            configuration = configuration
        )

    override fun sourcesRepository(configuration: SourcesFetchConfiguration, compositeDisposable: CompositeDisposable) =
        SourcesRepositoryImpl(
            newsApi = newsApi,
            sourcesDao = sourceDao,
            schedulersProvider = schedulersProvider,
            compositeDisposable = compositeDisposable,
            configuration = configuration
        )

}