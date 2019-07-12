package com.krendel.neusfeet.screens.common.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.krendel.neusfeet.screens.common.countryFlag
import com.krendel.neusfeet.screens.common.fitAndCrop

object ImageViewBindings {

    @BindingAdapter("countryFlag")
    @JvmStatic
    fun countryFlag(imageView: ImageView, countryCode: String?) {
        Glide.with(imageView)
            .countryFlag(countryCode)
            .fitAndCrop()
            .into(imageView)
    }

    @BindingAdapter("fitImageByUrl")
    @JvmStatic
    fun fitAndCropFromResource(imageView: ImageView, url: String?) {
        Glide.with(imageView)
            .load(url)
            .fitAndCrop()
            .into(imageView)
    }

}