package com.krendel.neusfeet.networking.sources

import com.google.gson.annotations.SerializedName
import com.krendel.neusfeet.local.source.Source

data class SourcesItem(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("language")
    val language: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)

fun SourcesItem.toSource(): Source = Source(
    id, country, name, description, language, category, url
)