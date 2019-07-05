package com.krendel.neusfeet.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.screens.common.BaseFragment
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeFragment : BaseFragment<HomeFragmentViewModel, HomeViewMvc>() {

    override val viewModel: HomeFragmentViewModel by viewModel()

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        lifecycleOwner: LifecycleOwner
    ): HomeViewMvc = get { parametersOf(inflater, container, lifecycleOwner) }

    override fun subscribeToViewModel(viewModel: HomeFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            // TODO view model events
            when (it) {

            }
        }
    }

    override fun subscribeToView(viewMvc: HomeViewMvc) {
        observe(viewMvc.eventsObservable) {
            // TODO view events
            when (it) {

            }
        }
    }
}