package com.krendel.neusfeet.common.di

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.screens.home.HomeViewMvc
import org.koin.dsl.module

val viewModule = module {

    factory { (inflater: LayoutInflater, container: ViewGroup?, lifecycleOwner: LifecycleOwner) ->
        HomeViewMvc(lifecycleOwner, inflater, container)
    }

}

val viewModelModule = module {



}