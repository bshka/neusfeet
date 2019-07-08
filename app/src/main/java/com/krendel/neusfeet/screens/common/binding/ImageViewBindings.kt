package com.krendel.neusfeet.screens.common.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.krendel.neusfeet.screens.common.fitAndCrop

object ImageViewBindings {

    @BindingAdapter("fitImageByUrl")
    @JvmStatic
    fun fitAndCropFromResource(imageView: ImageView, url: String) {
        Glide.with(imageView)
            .load(url)
            .fitAndCrop()
            .into(imageView)
    }

}