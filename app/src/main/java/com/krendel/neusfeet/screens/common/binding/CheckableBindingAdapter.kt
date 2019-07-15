package com.krendel.neusfeet.screens.common.binding

import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import com.krendel.neusfeet.screens.common.android_views.CheckableRelativeLayout

object CheckableBindingAdapter {

    @BindingAdapter("android:checked")
    @JvmStatic
    fun setChecked(view: CheckableRelativeLayout, checked: Boolean?) {
        if (checked != null && view.isChecked != checked) {
            view.isChecked = checked
        }
    }

    @BindingAdapter("android:onCheckedChanged")
    @JvmStatic
    fun setListener(view: CheckableRelativeLayout, listener: OnCheckedChangeListener?) {
        view.setOnCheckedChangeListener(listener)
    }

}
