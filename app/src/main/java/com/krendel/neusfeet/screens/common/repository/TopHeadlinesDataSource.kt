package com.krendel.neusfeet.screens.common.repository

import android.annotation.SuppressLint
import androidx.paging.PageKeyedDataSource
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.articles.toArticle
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.screens.common.binding.Listener
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class TopHeadlinesDataSource(
    private val newsApi: NewsApi,
    private val schedulersProvider: SchedulersProvider,
    private val compositeDisposable: CompositeDisposable,
    private val errorListener: Listener
) : PageKeyedDataSource<Int, Article>() {

    @SuppressLint("CheckResult")
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Article>) {
        compositeDisposable.add(
            loadHeadlines(1, params.requestedLoadSize)
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
                        errorListener()
                    }
                )
        )
    }

    @SuppressLint("CheckResult")
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        compositeDisposable.add(
            loadHeadlines(params.key, params.requestedLoadSize)
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
                        errorListener()
                    }
                )
        )
    }

    @SuppressLint("CheckResult")
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        compositeDisposable.add(
            loadHeadlines(params.key, params.requestedLoadSize)
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
                        errorListener()
                    }
                )
        )
    }

    @SuppressLint("CheckResult")
    private fun loadHeadlines(page: Int, pageSize: Int): Single<FetchTopHeadlinesResult> {
        return newsApi.headlines(
            pageSize = pageSize,
            page = page
        ).subscribeOn(schedulersProvider.io())
            .map { data ->
                val result = mutableListOf<Article>()
                data.articles?.forEach { schema ->
                    schema?.let {
                        result.add(it.toArticle())
                    }
                }
                val totalPages = (data.totalResults!! / pageSize) + 1
                FetchTopHeadlinesResult(
                    totalPages = totalPages,
                    totalResults = data.totalResults,
                    articles = result.toList()
                )
            }
            .observeOn(schedulersProvider.main())
    }

}

data class FetchTopHeadlinesResult(val totalPages: Int, val totalResults: Int, val articles: List<Article>)