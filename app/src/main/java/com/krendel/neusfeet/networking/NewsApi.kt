package com.krendel.neusfeet.networking

import com.krendel.neusfeet.networking.articles.ArticlesSchema
import com.krendel.neusfeet.networking.sources.SourceSchema
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines?language=en")
    fun headlines(@Query("page") page: Int = 1, @Query("pageSize") pageSize: Int = 20, @Query("q") query: String? = null): Single<ArticlesSchema>

    @GET("/v2/everything?language=en")
    fun everything(@Query("page") page: Int = 1, @Query("pageSize") pageSize: Int = 20, @Query("q") query: String? = null): Single<ArticlesSchema>

    @GET("/v2/sources")
    fun sources(
        @Query("category") category: String? = null,
        @Query("language") language: String? = null,
        @Query("country") country: String? = null
    ): Single<SourceSchema>

}