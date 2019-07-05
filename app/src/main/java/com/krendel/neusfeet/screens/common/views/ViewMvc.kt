package com.krendel.neusfeet.screens.common.views

import android.view.View
import androidx.annotation.LayoutRes

interface ViewMvc {
    @get:LayoutRes
    val layout: Int
    val rootView: View
}

interface ViewMvcActions