package com.krendel.neusfeet.screens.common.repository.topheadlines

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.krendel.neusfeet.local.source.SourceDao
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class TopHeadlinesRepository(
    private val sourceDao: SourceDao,
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

        compositeDisposable.add(
            sourceDao.sources()
                .subscribeOn(schedulersProvider.io())
                .observeOn(schedulersProvider.main())
                .subscribe(
                    { sources ->
                        configuration.sources = if (sources.isEmpty()) {
                            null
                        } else {
                            sources
                        }
                        sourceFactory.refresh()
                    },
                    {
                        Timber.e(it)
                    }
                )
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