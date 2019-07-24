package com.krendel.neusfeet.screens.common.repository.everything

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.articles.toArticle
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.repository.common.AppDataSource
import com.krendel.neusfeet.screens.common.repository.common.SourceInitialCallback
import com.krendel.neusfeet.screens.common.repository.common.SourceLoadCallback
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import kotlin.math.ceil

class EverythingDataSource(
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider,
    private val configuration: EverythingFetchConfiguration,
    compositeDisposable: CompositeDisposable
) : AppDataSource<Int, Article>(compositeDisposable) {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: SourceInitialCallback<Int, Article>
    ): Disposable =
        loadEverything(1, params.requestedLoadSize)
            .subscribe(
                { data ->

                    val nextPage = if (data.totalPages >= 2) {
                        2
                    } else {
                        null
                    }

                    if (params.placeholdersEnabled) {
                        callback.onResult(data.articles, 0, data.totalResults, null, nextPage)
                    } else {
                        callback.onResult(data.articles, null, nextPage)
                    }
                },
                {
                    Timber.e(it)
                    callback.onError(it)
                }
            )

    override fun loadAfter(params: LoadParams<Int>, callback: SourceLoadCallback<Int, Article>): Disposable =
        loadEverything(params.key, params.requestedLoadSize)
            .subscribe(
                { data ->

                    // if it is not last page, provide next page
                    val nextPage = if (data.totalPages > params.key) {
                        params.key + 1
                    } else {
                        null
                    }

                    callback.onResult(data.articles, nextPage)
                },
                {
                    Timber.e(it)
                    callback.onError(it)
                }
            )

    override fun loadBefore(params: LoadParams<Int>, callback: SourceLoadCallback<Int, Article>): Disposable =
        loadEverything(params.key, params.requestedLoadSize)
            .observeOn(schedulersProvider.main())
            .subscribe(
                { data ->

                    // if it is not last page, provide next page
                    val prevKey = if (params.key > 1) {
                        params.key - 1
                    } else {
                        null
                    }

                    callback.onResult(data.articles, prevKey)
                },
                {
                    Timber.e(it)
                    callback.onError(it)
                }
            )

    private fun loadEverything(page: Int, pageSize: Int): Single<FetchEverythingResult> {
        return newsApi.everything(
            pageSize = pageSize,
            page = page,
            query = configuration.query
        ).subscribeOn(schedulersProvider.io())
            .map { data ->
                val result = mutableListOf<Article>()
                data.articles?.forEach { schema ->
                    schema?.let {
                        result.add(it.toArticle())
                    }
                }
                val delta = data.totalResults!! % pageSize.toDouble()
                val totalPages = data.totalResults / pageSize + if (delta > 0) 1 else 0
                FetchEverythingResult(
                    totalPages = totalPages,
                    totalResults = data.totalResults,
                    articles = result.toList()
                )
            }
            .observeOn(schedulersProvider.main())
    }

}

private data class FetchEverythingResult(val totalPages: Int, val totalResults: Int, val articles: List<Article>)