package com.krendel.neusfeet.screens.common.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BaseObservable
import androidx.databinding.ViewDataBinding

abstract class ListItemViewMvc<B : ViewDataBinding, A : ViewMvcActions>(
    inflater: LayoutInflater,
    container: ViewGroup?
): BaseViewMvc<B, A>(inflater, container) {

    val viewType: Int get() = layout
    abstract val id: Long

}