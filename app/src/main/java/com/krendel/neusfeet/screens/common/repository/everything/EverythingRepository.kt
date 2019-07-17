package com.krendel.neusfeet.screens.common.repository.everything

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.disposables.CompositeDisposable

class EverythingRepository(
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider,
    private val compositeDisposable: CompositeDisposable,
    private val configuration: EverythingFetchConfiguration
) {

    fun everything(pageSize: Int): PagedListing<ArticleItemViewModel> {
        val sourceFactory = EverythingDataSourceFactory(
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
            ).setFetchScheduler(schedulersProvider.main())
                .buildObservable(),
            refresh = {
                sourceFactory.refresh()
            },
            dataSourceActions = sourceFactory.eventsObservable()
        )
    }

}