package com.krendel.neusfeet.screens.common.repository.topheadlines

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.disposables.CompositeDisposable

class TopHeadlinesRepository(
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider,
    private val compositeDisposable: CompositeDisposable,
    private val configuration: TopHeadlinesFetchConfiguration
) {

    fun headlines(pageSize: Int): PagedListing<ArticleItemViewModel> {

        val sourceFactory = TopHeadlinesDataSourceFactory(
            newsApi = newsApi,
            schedulersProvider = schedulersProvider,
            compositeDisposable = compositeDisposable,
            configuration = configuration
        )

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()

        return PagedListing(
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