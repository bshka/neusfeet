package com.krendel.neusfeet.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.krendel.neusfeet.R
import com.krendel.neusfeet.databinding.ViewHomeBinding
import com.krendel.neusfeet.screens.common.views.BaseViewMvc
import com.krendel.neusfeet.screens.common.views.ViewMvcActions

class HomeViewMvc(
    lifecycleOwner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
) : BaseViewMvc<ViewHomeBinding, HomeViewActions>(lifecycleOwner, inflater, container) {

    override val layout: Int = R.layout.view_home

    override fun create() {

    }
}

sealed class HomeViewActions : ViewMvcActions {

}