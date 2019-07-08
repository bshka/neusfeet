package com.krendel.neusfeet.screens.common.usecase

import android.accounts.NetworkErrorException
import com.krendel.neusfeet.model.Article
import com.krendel.neusfeet.model.Source
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.articles.toArticle
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import io.reactivex.Single

class FetchTopHeadlinesUseCase(
    private val api: NewsApi,
    private val schedulers: SchedulersProvider
) {

    fun fetch(): Single<List<Article>> =
        api.headlines()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.computation())
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
                result
            }

}

class FetchTopHeadlinesException(t: String): NetworkErrorException(t)