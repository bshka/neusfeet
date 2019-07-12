package com.krendel.neusfeet.screens.settings

import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewSettingsBinding
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.ViewMvcActions

class SettingsViewMvc(
    configuration: LifecycleViewMvcConfiguration
) : BaseLifecycleViewMvc<LifecycleViewMvcConfiguration, ViewSettingsBinding, SettingsViewActions>(configuration) {

    override val layout: Int = R.layout.view_settings

    override fun bindViewModel(dataBinding: ViewSettingsBinding) {
        dataBinding.viewModel = this
    }


}

sealed class SettingsViewActions : ViewMvcActions