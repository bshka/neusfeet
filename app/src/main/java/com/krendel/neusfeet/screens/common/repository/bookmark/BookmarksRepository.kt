package com.krendel.neusfeet.screens.common.repository.bookmark

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.krendel.neusfeet.local.bookmarks.BookmarksDao
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.common.PagedListing
import com.krendel.neusfeet.screens.common.views.articles.ArticleItemViewModel
import io.reactivex.Observable

class BookmarksRepository(
    private val bookmarksDao: BookmarksDao,
    private val schedulersProvider: SchedulersProvider
) {

    fun bookmarks(pageSize: Int): PagedListing<ArticleItemViewModel> {
        val sourceFactory = bookmarksDao.all()

        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize)
            .setEnablePlaceholders(false)
            .build()

        return PagedListing(
            dataList = RxPagedListBuilder<Int, ArticleItemViewModel>(
                sourceFactory.map { ArticleItemViewModel(it) },
                config
            ).setFetchScheduler(schedulersProvider.io())
                .setNotifyScheduler(schedulersProvider.main())
                .buildObservable(),
            refresh = {
                // nothing
            },
            dataSourceActions = Observable.empty()
        )
    }

}