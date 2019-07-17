package com.krendel.neusfeet.screens.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.ViewFlipper
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.krendel.neusfeet.R
import com.krendel.neusfeet.common.NewsApplication

fun RequestManager.countryFlag(code: String?): RequestBuilder<Drawable> =
    this.load("https://lipis.github.io/flag-icon-css/flags/4x3/$code.svg")

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

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context.getAttributeDimension(attr: Int): Int {
    val typedArray = theme.obtainStyledAttributes(intArrayOf(attr))
    val dimension = typedArray.getDimensionPixelOffset(0, 0)
    typedArray.recycle()
    return dimension
}

fun ViewFlipper.switchToChild(index: Int) {
    if (index != displayedChild) {
        displayedChild = index
    }
}