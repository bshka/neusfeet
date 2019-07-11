package com.krendel.neusfeet.screens.common.repository

import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesFetchConfiguration
import com.krendel.neusfeet.screens.common.repository.topheadlines.TopHeadlinesRepository
import io.reactivex.disposables.CompositeDisposable

class RepositoryFactory(
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

}