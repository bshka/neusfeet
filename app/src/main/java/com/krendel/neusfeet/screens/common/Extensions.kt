package com.krendel.neusfeet.screens.common

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.krendel.neusfeet.common.NewsApplication

fun RequestBuilder<Drawable>.fitAndCrop(@DrawableRes placeholder: Int = -1): RequestBuilder<Drawable> =
    this.apply(RequestOptions().fitCenter().centerCrop())
        .thumbnail(
            Glide.with(NewsApplication.instance)
                .load(placeholder)
                .apply(RequestOptions().fitCenter().centerCrop())
        )