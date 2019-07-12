package com.krendel.neusfeet.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewMainBinding
import com.krendel.neusfeet.screens.common.views.BaseLifecycleViewMvc
import com.krendel.neusfeet.screens.common.views.LifecycleViewMvcConfiguration
import com.krendel.neusfeet.screens.common.views.ViewMvcActions

class MainViewMvc(
    configuration: MainViewMvcConfiguration
) : BaseLifecycleViewMvc<MainViewMvcConfiguration, ViewMainBinding, MainViewActions>(configuration) {

    override val layout: Int = R.layout.view_main

    override fun bindViewModel(dataBinding: ViewMainBinding) {
        dataBinding.viewModel = this
    }

    override fun create() {
        super.create().let { }
        val host = configuration.fragmentManager
            .findFragmentById(R.id.nav_fragment) as NavHostFragment? ?: return
        dataBinding.bottomNavigation.setupWithNavController(host.navController)
    }

}

sealed class MainViewActions : ViewMvcActions

data class MainViewMvcConfiguration(
    val fragmentManager: FragmentManager,
    override val lifecycleOwner: LifecycleOwner,
    override val inflater: LayoutInflater,
    override val container: ViewGroup?
) : LifecycleViewMvcConfiguration