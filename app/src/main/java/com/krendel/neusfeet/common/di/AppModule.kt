package com.krendel.neusfeet.common.di

import com.google.gson.GsonBuilder
import com.krendel.neusfeet.BuildConfig
import com.krendel.neusfeet.local.AppDatabase
import com.krendel.neusfeet.networking.NewsApi
import com.krendel.neusfeet.networking.interceptor.ApiInterceptor
import com.krendel.neusfeet.networking.schedulers.SchedulersProvider
import com.krendel.neusfeet.networking.schedulers.SchedulersProviderImpl
import com.krendel.neusfeet.screens.bookmarks.BookmarksFragmentViewModel
import com.krendel.neusfeet.screens.bookmarks.BookmarksViewMvc
import com.krendel.neusfeet.screens.common.repository.RepositoryFactory
import com.krendel.neusfeet.screens.common.repository.bookmark.AddBookmarkUseCase
import com.krendel.neusfeet.screens.common.repository.bookmark.RemoveBookmarkUseCase
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import com.krendel.neusfeet.screens.home.HomeFragmentViewModel
import com.krendel.neusfeet.screens.home.HomeViewMvc
import com.krendel.neusfeet.screens.main.MainActivityViewModel
import com.krendel.neusfeet.screens.main.MainViewMvc
import com.krendel.neusfeet.screens.main.MainViewMvcConfiguration
import com.krendel.neusfeet.screens.preview.PreviewFragmentViewModel
import com.krendel.neusfeet.screens.preview.PreviewViewConfiguration
import com.krendel.neusfeet.screens.preview.PreviewViewMvc
import com.krendel.neusfeet.screens.search.SearchFragmentViewModel
import com.krendel.neusfeet.screens.search.SearchViewMvc
import com.krendel.neusfeet.screens.settings.SettingsFragmentViewModel
import com.krendel.neusfeet.screens.settings.SettingsViewMvc
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
private val DATABASE = StringQualifier("DATABASE")
private val REPOSITORY_FACTORY = StringQualifier("REPOSITORY_FACTORY")

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

val databaseModule = module {

    single(DATABASE) { AppDatabase.getInstance(androidContext()) }

    single { (get(DATABASE) as AppDatabase).sourceDao() }
    single { (get(DATABASE) as AppDatabase).bookmarksDao() }

}

val repoModule = module {

    single(REPOSITORY_FACTORY) { RepositoryFactory(get(), get(), get(), get()) }

    factory { (get(REPOSITORY_FACTORY) as RepositoryFactory).bookmarksRepository() }

    single { AddBookmarkUseCase(get(), get()) }
    single { RemoveBookmarkUseCase(get(), get()) }

}

val viewModule = module {

    // article view
    factory { (configuration: PreviewViewConfiguration) -> PreviewViewMvc(configuration) }

    // settings view
    factory { (configuration: LifecycleViewMvcConfiguration) -> SettingsViewMvc(configuration) }

    // bookmarks view
    factory { (configuration: LifecycleViewMvcConfiguration) -> BookmarksViewMvc(configuration) }

    // search view
    factory { (configuration: LifecycleViewMvcConfiguration) -> SearchViewMvc(configuration) }

    // home view
    factory { (configuration: LifecycleViewMvcConfiguration) -> HomeViewMvc(configuration) }

    // main view
    factory { (configuration: MainViewMvcConfiguration) -> MainViewMvc(configuration) }

}

val viewModelModule = module {

    // main activity
    viewModel { MainActivityViewModel() }

    // fragments

    // home fragment
    viewModel { HomeFragmentViewModel(get(), get(REPOSITORY_FACTORY), get()) }
    // search fragment
    viewModel { SearchFragmentViewModel(get(), get(REPOSITORY_FACTORY), get()) }
    // article preview fragment
    viewModel { PreviewFragmentViewModel(get(), get()) }
    // bookmarks fragment
    viewModel { BookmarksFragmentViewModel(get(), get(), get()) }
    // settings fragment
    viewModel { SettingsFragmentViewModel(get(REPOSITORY_FACTORY), get()) }

}