package com.krendel.neusfeet.screens.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewSettingsBinding
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions

class SettingsViewMvc(
    inflater: LayoutInflater,
    container: ViewGroup?,
    lifecycleOwner: LifecycleOwner
) : BaseLifecycleViewMvc<ViewSettingsBinding, SettingsViewActions>(inflater, container, lifecycleOwner) {

    override val layout: Int = R.layout.view_settings

    override fun bindViewModel(dataBinding: ViewSettingsBinding) {
        dataBinding.viewModel = this
    }


}

sealed class SettingsViewActions : ViewMvcActions