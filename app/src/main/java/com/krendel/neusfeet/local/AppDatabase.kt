package com.krendel.neusfeet.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.krendel.neusfeet.local.source.Source
import com.krendel.neusfeet.local.source.SourceDao

@Database(entities = [Source::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sourceDao(): SourceDao

    companion object {

        fun getInstance(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "database").build()

    }

}