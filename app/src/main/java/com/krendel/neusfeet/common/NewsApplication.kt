package com.krendel.neusfeet.common

import android.app.Application
import com.krendel.neusfeet.common.di.viewModelModule
import com.krendel.neusfeet.common.di.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NewsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO
        startKoin {
            androidContext(this@NewsApplication)
            androidLogger()
            modules(
                listOf(
                    viewModelModule, viewModule
                )
            )
        }
    }

}