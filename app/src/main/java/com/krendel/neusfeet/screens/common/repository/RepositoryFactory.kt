package com.krendel.neusfeet.screens.common.repository

import com.krendel.neusfeet.local.source.SourceDao
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.everything.EverythingFetchConfiguration
import com.krendel.neusfeet.screens.common.repository.everything.EverythingRepository
import com.krendel.neusfeet.screens.common.repository.sources.SourcesFetchConfiguration
import com.krendel.neusfeet.screens.common.repository.sources.SourcesRepository
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesFetchConfiguration
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesRepository
import io.reactivex.disposables.CompositeDisposable

class RepositoryFactory(
    private val sourceDao: SourceDao,
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider
) {

    fun topHeadlinesRepository(
        configuration: TopHeadlinesFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ) =
        TopHeadlinesRepository(
            newsApi = newsApi,
            schedulersProvider = schedulersProvider,
            compositeDisposable = compositeDisposable,
            configuration = configuration
        )

    fun everythingRepository(
        configuration: EverythingFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ) =
        EverythingRepository(
            newsApi = newsApi,
            schedulersProvider = schedulersProvider,
            compositeDisposable = compositeDisposable,
            configuration = configuration
        )

    fun sourcesRepository(
        configuration: SourcesFetchConfiguration,
        compositeDisposable: CompositeDisposable
    ) =
        SourcesRepository(
            newsApi = newsApi,
            sourcesDao = sourceDao,
            schedulersProvider = schedulersProvider,
            compositeDisposable = compositeDisposable,
            configuration = configuration
        )

}