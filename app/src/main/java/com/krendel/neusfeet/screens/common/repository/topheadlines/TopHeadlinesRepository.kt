package com.krendel.neusfeet.screens.common.repository.topheadlines

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import com.krendel.neusfeet.screens.common.repository.common.Listing
import io.reactivex.disposables.CompositeDisposable

class TopHeadlinesRepository(
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider,
    private val compositeDisposable: CompositeDisposable
) {

    fun headlines(pageSize: Int): Listing<ArticleItemViewModel> {

        val sourceFactory = TopHeadlinesDatasourceFactory(
            newsApi = newsApi,
            schedulersProvider = schedulersProvider,
            compositeDisposable = compositeDisposable
        )

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()

        return Listing(
            dataList = RxPagedListBuilder<Int, ArticleItemViewModel>(
                sourceFactory.map { ArticleItemViewModel(it) },
                config
            )
                .setFetchScheduler(schedulersProvider.main())
                .buildObservable(),
            refresh = {
                sourceFactory.refresh()
            },
            dataSourceActions = sourceFactory.eventsObservable()
        )
    }

}