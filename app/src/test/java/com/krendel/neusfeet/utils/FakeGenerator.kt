package com.krendel.neusfeet.utils

import com.krendel.neusfeet.model.articles.Article
import com.krendel.neusfeet.networking.articles.ArticlesSchema
import java.util.*

object FakeGenerator {

    fun getArticle(publishedAt: Date) = Article(
        localId = 123,
        content = "content",
        urlToImage = "url to image",
        title = "title",
        sourceId = "source id",
        sourceName = "source name",
        description = "description",
        url = "url",
        author = "author",
        publishedAt = publishedAt
    )

    fun getArticlesSchema(itemsCount: Int = 1) = ArticlesSchema(
        totalResults = itemsCount,
        articles = listOf(),
        code = "200",
        message = "message",
        status = "status"
    )

}