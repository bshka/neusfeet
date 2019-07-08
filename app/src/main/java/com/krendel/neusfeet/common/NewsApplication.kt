package com.krendel.neusfeet.common

import android.app.Application
import com.krendel.neusfeet.BuildConfig
import com.krendel.neusfeet.common.di.rxJava
import com.krendel.neusfeet.common.di.useCaseModule
import com.krendel.neusfeet.common.di.viewModelModule
import com.krendel.neusfeet.common.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@NewsApplication)
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            modules(
                listOf(
                    viewModelModule, viewModule, rxJava, useCaseModule
                )
            )
        }
    }

    companion object {
        lateinit var instance: NewsApplication
    }

}