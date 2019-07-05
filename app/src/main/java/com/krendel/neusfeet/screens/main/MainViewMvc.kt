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
import com.krendel.neusfeet.screens.common.views.ViewMvcActions

class MainViewMvc(
    private val fragmentManager: FragmentManager,
    lifecycleOwner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseLifecycleViewMvc<ViewMainBinding, MainViewActions>(lifecycleOwner, inflater, container) {

    override val layout: Int = R.layout.view_main

    override fun create() {
        val host = fragmentManager
            .findFragmentById(R.id.nav_fragment) as NavHostFragment? ?: return
        dataBinding.bottomNavigation.setupWithNavController(host.navController)
    }

}

sealed class MainViewActions : ViewMvcActions