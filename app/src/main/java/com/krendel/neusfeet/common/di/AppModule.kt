package com.krendel.neusfeet.common.di

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.google.gson.GsonBuilder
import com.krendel.neusfeet.BuildConfig
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.interceptor.ApiInterceptor
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.networking.schedulers.SchedulersProviderImpl
import com.krendel.neusfeet.screens.common.repository.RepositoryFactory
import com.krendel.neusfeet.screens.home.HomeFragmentViewModel
import com.krendel.neusfeet.screens.home.HomeViewMvc
import com.krendel.neusfeet.screens.main.MainActivityViewModel
import com.krendel.neusfeet.screens.main.MainViewMvc
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

private val RETROFIT = StringQualifier("RETROFIT")

val rxJava = module {

    single { SchedulersProviderImpl() as SchedulersProvider }

    single(RETROFIT) {
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        val httpCacheDirectory = File(androidContext().cacheDir, "http-cache")
        val cache = Cache(httpCacheDirectory, cacheSize)

        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        val httpClient = OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(ApiInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

        Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    single { (get(RETROFIT) as Retrofit).create(NewsApi::class.java) }

}

val repoModule = module {

    single { RepositoryFactory(get(), get()) }

}

val viewModule = module {

    factory { (inflater: LayoutInflater, container: ViewGroup?, lifecycleOwner: LifecycleOwner) ->
        HomeViewMvc(lifecycleOwner, inflater, container)
    }

    factory { (fm: FragmentManager, inflater: LayoutInflater, container: ViewGroup?, lifecycleOwner: LifecycleOwner) ->
        MainViewMvc(fm, lifecycleOwner, inflater, container)
    }

}

val viewModelModule = module {

    // main activity
    viewModel { MainActivityViewModel() }

    // fragments

    // home fragment
    viewModel { HomeFragmentViewModel(get(), get()) }

}