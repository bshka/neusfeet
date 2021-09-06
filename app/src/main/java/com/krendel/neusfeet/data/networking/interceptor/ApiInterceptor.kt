package com.krendel.neusfeet.data.networking.interceptor

import com.krendel.neusfeet.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestBuilder = originalRequest.newBuilder().url(originalRequest.url()).addHeader("X-Api-Key", BuildConfig.API_KEY)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}