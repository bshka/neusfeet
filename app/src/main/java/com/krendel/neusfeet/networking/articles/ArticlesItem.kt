package com.krendel.neusfeet.networking.articles

import com.google.gson.annotations.SerializedName
import com.krendel.neusfeet.model.articles.Article
import java.util.*

data class ArticlesItem(

    @field:SerializedName("publishedAt")
    val publishedAt: Date? = null,

    @field:SerializedName("author")
    val author: String? = null,

    @field:SerializedName("urlToImage")
    val urlToImage: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("source")
    val sourceSchema: SourceSchema? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("url")
    val url: String? = null,

    @field:SerializedName("content")
    val content: String? = null
)

fun ArticlesItem.toArticle(): Article = Article(
    publishedAt = publishedAt,
    author = author,
    url = url,
    description = description,
    sourceName = sourceSchema?.name,
    sourceId = sourceSchema?.id,
    title = title,
    urlToImage = urlToImage,
    content = content
)
