package com.krendel.neusfeet.screens.common.binding

import android.view.View
import androidx.databinding.BindingAdapter

object ViewBindings {

    @BindingAdapter("android:minHeight")
    @JvmStatic
    fun minHeight(view: View, minHeight: Int) {
        view.minimumHeight = minHeight
    }


}