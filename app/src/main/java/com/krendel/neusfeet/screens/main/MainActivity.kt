package com.krendel.neusfeet.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.screens.common.BaseActivity
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : BaseActivity<MainActivityViewModel, MainViewMvc>() {

    override val viewModel: MainActivityViewModel by viewModel()

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        lifecycleOwner: LifecycleOwner
    ): MainViewMvc = get { parametersOf(supportFragmentManager, inflater, container, lifecycleOwner) }

    override fun subscribeToView(viewMvc: MainViewMvc) {
        observe(viewMvc.eventsObservable) {
            // TODO view event
            when (it) {

            }
        }
    }

    override fun subscribeToViewModel(viewModel: MainActivityViewModel) {
        observe(viewModel.eventsObservable) {
            // TODO view model events
            when (it) {

            }
        }
    }

}
