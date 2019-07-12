package com.krendel.neusfeet.screens.common.repository.everything

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.common.AppDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class EverythingDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider,
    private val configuration: EverythingFetchConfiguration
) : AppDataSourceFactory<EverythingDataSource, Article>() {

    override val dataSource: BehaviorSubject<EverythingDataSource> = BehaviorSubject.create()

    override fun newSource(): EverythingDataSource =
        EverythingDataSource(
            newsApi,
            schedulersProvider,
            configuration,
            compositeDisposable
        )
}

data class EverythingFetchConfiguration(
    var pageSize: Int,
    var query: String? = null
)