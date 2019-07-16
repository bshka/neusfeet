package com.krendel.neusfeet.local.bookmarks

import androidx.paging.DataSource
import androidx.room.*
import com.krendel.neusfeet.model.articles.Article

@Dao
interface BookmarksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(article: Article): Long

    @Delete
    fun delete(article: Article): Int

    @Query("SELECT * FROM article ORDER BY localId DESC")
    fun all(): DataSource.Factory<Int, Article>

}