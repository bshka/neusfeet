package com.krendel.neusfeet.screens.main

import android.view.View
import com.krendel.neusfeet.screens.common.BaseActivity
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity<MainActivityViewModel, MainViewMvc>() {

    override val viewModel: MainActivityViewModel by viewModel()

    override fun createView(configuration: LifecycleViewMvcConfiguration): MainViewMvc {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        return get {
            parametersOf(
                MainViewMvcConfiguration(
                    fragmentManager = supportFragmentManager,
                    lifecycleOwner = configuration.lifecycleOwner,
                    inflater = configuration.inflater,
                    container = configuration.container
                )
            )
        }
    }

    override fun subscribeToView(viewMvc: MainViewMvc) {
        observe(viewMvc.eventsObservable) {
            // no actions from view mvc
//            when (it) {
//
//            }
        }
    }

    override fun subscribeToViewModel(viewModel: MainActivityViewModel) {
        observe(viewModel.eventsObservable) {
            // no actions from view model
//            when (it) {
//
//            }
        }
    }

}
