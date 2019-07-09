package com.krendel.neusfeet.screens.common.repository

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.binding.Listener
import com.krendel.neusfeet.screens.common.list.ArticleItemViewModel
import io.reactivex.disposables.CompositeDisposable

class TopHeadlinesRepository(
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider,
    private val compositeDisposable: CompositeDisposable,
    private val errorListener: Listener
) {

    fun headlines(pageSize: Int): Listing<ArticleItemViewModel> {

        val sourceFactory = TopHeadlinesDatasourceFactory(
            newsApi = newsApi,
            schedulersProvider = schedulersProvider,
            compositeDisposable = compositeDisposable,
            errorListener = errorListener
        ).map { ArticleItemViewModel(it) }

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()

        return Listing(
            dataList = RxPagedListBuilder<Int, ArticleItemViewModel>(sourceFactory, config).buildObservable(),
            refresh = {
                (sourceFactory as TopHeadlinesDatasourceFactory).dataSource.value?.invalidate()
            },
            error = errorListener
        )
    }

}