package com.krendel.neusfeet.screens.common.repository

import androidx.paging.DataSource
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.binding.Listener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

class TopHeadlinesDatasourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider,
    private val errorListener: Listener
) : DataSource.Factory<Int, Article>() {

    val dataSource = BehaviorSubject.create<TopHeadlinesDataSource>()

    override fun create(): DataSource<Int, Article> {
        val source = TopHeadlinesDataSource(newsApi, schedulersProvider, compositeDisposable, errorListener)
        dataSource.onNext(source)
        return source
    }

}