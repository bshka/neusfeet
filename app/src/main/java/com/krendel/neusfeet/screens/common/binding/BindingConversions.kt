package com.krendel.neusfeet.screens.common.binding

import android.view.View
import androidx.databinding.BindingConversion

/**
 * @author Alexey Zubkovskiy on 04 Jun 2019
 */

@BindingConversion
fun convertLambdaToClickListener(listener: Listener?): View.OnClickListener =
    View.OnClickListener { listener?.invoke() }