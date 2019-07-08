package com.krendel.neusfeet.networking.articles

import com.google.gson.annotations.SerializedName
import com.krendel.neusfeet.model.Source

data class SourceSchema(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

fun SourceSchema.toSource(): Source = Source(name, id)