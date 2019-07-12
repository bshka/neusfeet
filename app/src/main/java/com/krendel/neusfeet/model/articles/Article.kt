package com.krendel.neusfeet.model.articles

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Article(
    val publishedAt: Date? = null,
    val author: String? = null,
    val urlToImage: String? = null,
    val description: String? = null,
    val source: Source? = null,
    val title: String? = null,
    val url: String? = null,
    val content: String? = null
): Parcelable