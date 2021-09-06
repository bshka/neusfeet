package com.krendel.neusfeet.data.articles

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.krendel.neusfeet.data.local.Converters
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
@TypeConverters(Converters::class)
data class Article(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0,
    val publishedAt: Date? = null,
    val author: String? = null,
    val urlToImage: String? = null,
    val description: String? = null,
    val sourceName: String? = null,
    val sourceId: String? = null,
    val title: String? = null,
    val url: String? = null,
    val content: String? = null
) : Parcelable
