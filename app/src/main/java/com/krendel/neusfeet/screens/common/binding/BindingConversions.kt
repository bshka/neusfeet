package com.krendel.neusfeet.screens.common.binding

import android.view.View
import androidx.databinding.BindingConversion

@BindingConversion
fun convertLambdaToClickListener(listener: Listener?): View.OnClickListener =
    View.OnClickListener { listener?.invoke() }