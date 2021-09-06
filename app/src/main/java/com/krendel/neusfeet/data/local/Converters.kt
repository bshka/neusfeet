package com.krendel.neusfeet.data.local

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun dateToLong(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun longToDate(str: Long): Date {
        return try {
            Date(str)
        } catch (e: Exception) {
            Date()
        }
    }

}