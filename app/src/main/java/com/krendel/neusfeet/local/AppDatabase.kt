package com.krendel.neusfeet.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.krendel.neusfeet.local.bookmarks.BookmarksDao
import com.krendel.neusfeet.local.source.Source
import com.krendel.neusfeet.local.source.SourceDao
import com.krendel.neusfeet.model.articles.Article

@Database(entities = [Source::class, Article::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sourceDao(): SourceDao
    abstract fun bookmarksDao(): BookmarksDao

    companion object {

        fun getInstance(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "database").build()

    }

}