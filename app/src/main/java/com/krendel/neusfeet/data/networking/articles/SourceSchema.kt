package com.krendel.neusfeet.data.networking.articles

import com.google.gson.annotations.SerializedName

data class SourceSchema(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null
)