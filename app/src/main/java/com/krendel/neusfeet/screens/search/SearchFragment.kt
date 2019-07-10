package com.krendel.neusfeet.screens.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.screens.common.BaseFragment
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : BaseFragment<SearchFragmentViewModel, SearchViewMvc>() {

    override val viewModel: SearchFragmentViewModel by viewModel()

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        lifecycleOwner: LifecycleOwner
    ): SearchViewMvc = get { parametersOf(inflater, container, lifecycleOwner) }

    override fun subscribeToViewModel(viewModel: SearchFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            // TODO observe view model actions
            when (it) {

            }
        }
    }

    override fun subscribeToView(viewMvc: SearchViewMvc) {
        observe(viewMvc.eventsObservable) {
            // TODO observer view actions
            when (it) {

            }
        }
    }
}