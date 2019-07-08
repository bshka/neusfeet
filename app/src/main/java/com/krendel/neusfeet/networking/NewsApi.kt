package com.krendel.neusfeet.networking

import com.krendel.neusfeet.networking.articles.ArticlesSchema
import com.krendel.neusfeet.networking.sources.SourceSchema
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("/v2/top-headlines?language=ru")
    fun headlines(@Query( "page") page: Int = 1, @Query("pageSize") pageSize: Int = 20): Single<ArticlesSchema>

    @GET("/v2/everything")
    fun everything(): Single<ArticlesSchema>

    @GET("/v2/sources")
    fun sources(): Single<SourceSchema>

}