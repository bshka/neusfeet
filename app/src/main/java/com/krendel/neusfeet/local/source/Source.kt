package com.krendel.neusfeet.local.source

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Source(
    @PrimaryKey
    val id: String,
    val country: String? = null,
    val name: String? = null,
    val description: String? = null,
    val language: String? = null,
    val category: String? = null,
    val url: String? = null
)