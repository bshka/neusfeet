package com.krendel.neusfeet.screens.common.repository.topheadlines

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.common.AppDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class TopHeadlinesDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider,
    private val configuration: TopHeadlinesFetchConfiguration
) : AppDataSourceFactory<TopHeadlinesDataSource, Article>() {

    override val dataSource: BehaviorSubject<TopHeadlinesDataSource> = BehaviorSubject.create()

    override fun newSource(): TopHeadlinesDataSource =
        TopHeadlinesDataSource(
            newsApi = newsApi,
            schedulersProvider = schedulersProvider,
            configuration = configuration,
            compositeDisposable = compositeDisposable
        )

}

data class TopHeadlinesFetchConfiguration(
    var pageSize: Int,
    var query: String? = null,
    var sources: String? = null
)