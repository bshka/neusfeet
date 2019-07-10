package com.krendel.neusfeet.screens.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import com.krendel.neusfeet.R
import com.krendel.neusfeet.common.NewsApplication

fun RequestBuilder<Drawable>.fitAndCrop(): RequestBuilder<Drawable> =
    this.apply(RequestOptions().fitCenter().centerCrop())
        .error(
            Glide.with(NewsApplication.instance)
                .load(R.drawable.placeholder)
                .apply(RequestOptions().centerInside())
        )

fun Context.applyDP(px: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.displayMetrics).toInt()

fun View.applyDP(px: Float): Int = this.context.applyDP(px)