package com.krendel.neusfeet.utils

import com.krendel.neusfeet.model.articles.Article
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

}