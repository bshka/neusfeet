package com.krendel.neusfeet.screens.settings

import com.krendel.neusfeet.screens.common.BaseFragment
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SettingsFragment : BaseFragment<SettingsFragmentViewModel, SettingsViewMvc>() {

    override val viewModel: SettingsFragmentViewModel by viewModel()

    override fun createView(configuration: LifecycleViewMvcConfiguration): SettingsViewMvc =
        get { parametersOf(configuration) }

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