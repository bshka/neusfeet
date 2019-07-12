package com.krendel.neusfeet.screens.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.screens.common.BaseFragment
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsFragment : BaseFragment<SettingsFragmentViewModel, SettingsViewMvc>() {

    override val viewModel: SettingsFragmentViewModel by viewModel()

    override fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        lifecycleOwner: LifecycleOwner
    ): SettingsViewMvc = get { parametersOf(inflater, container, lifecycleOwner) }

    override fun subscribeToViewModel(viewModel: SettingsFragmentViewModel) {
        observe(viewModel.eventsObservable) {
            // TODO actions for view model
            when (it) {

            }
        }
    }

    override fun subscribeToView(viewMvc: SettingsViewMvc) {
        observe(viewMvc.eventsObservable) {
            // TODO actions for view
            when (it) {

            }
        }
    }
}