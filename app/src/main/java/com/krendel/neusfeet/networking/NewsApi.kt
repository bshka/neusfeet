package com.krendel.neusfeet.networking

import com.krendel.neusfeet.networking.articles.ArticlesSchema
import com.krendel.neusfeet.networking.sources.SourceSchema
import io.reactivex.Single
import retrofit2.http.GET

interface NewsApi {

    @GET("/v2/top-headlines")
    fun headlines(): Single<ArticlesSchema>

    @GET("/v2/everything")
    fun everything(): Single<ArticlesSchema>

    @GET("/v2/sources")
    fun sources(): Single<SourceSchema>

}