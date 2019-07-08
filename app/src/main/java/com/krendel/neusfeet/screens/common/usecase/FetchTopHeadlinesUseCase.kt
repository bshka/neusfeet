package com.krendel.neusfeet.screens.common.usecase

import android.accounts.NetworkErrorException
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.articles.toArticle
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import io.reactivex.Single

class FetchTopHeadlinesUseCase(
    private val api: NewsApi,
    private val schedulers: SchedulersProvider
) {

    fun fetch(page: Int = 1): Single<FetchTopHeadlinesResult> =
        api.headlines(page, PAGE_SIZE)
            .subscribeOn(schedulers.io())
            .map { data ->
                if ("ok" != data.status) {
                    throw FetchTopHeadlinesException("Api error")
                }

                val result = mutableListOf<Article>()
                data.articles?.forEach { schema ->
                    schema?.let {
                        result.add(it.toArticle())
                    }
                }
                val totalPages = (data.totalResults!! / PAGE_SIZE) + 1
                FetchTopHeadlinesResult(totalPages, result.toList())
            }

    companion object {
        const val PAGE_SIZE = 20
    }

}

data class FetchTopHeadlinesResult(val totalPages: Int, val articles: List<Article>)
class FetchTopHeadlinesException(t: String) : NetworkErrorException(t)