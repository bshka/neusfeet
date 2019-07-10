package com.krendel.neusfeet.screens.common.repository.topheadlines

import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.common.AppDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class TopHeadlinesDatasourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider
) : AppDataSourceFactory<TopHeadlinesDataSource, Article>() {

    override val dataSource: BehaviorSubject<TopHeadlinesDataSource> = BehaviorSubject.create()

    override fun newSource(): TopHeadlinesDataSource =
        TopHeadlinesDataSource(
            newsApi,
            schedulersProvider,
            compositeDisposable
        )

}