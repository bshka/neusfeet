package com.krendel.neusfeet.networking.sources

import com.google.gson.annotations.SerializedName
import com.krendel.neusfeet.model.source.Source

data class SourcesItem(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("language")
    val language: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("url")
    val url: String? = null
)

fun SourcesItem.toSource(): Source = Source(
    country, name, description, language, id, category, url
)