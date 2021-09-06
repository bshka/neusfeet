package com.krendel.neusfeet.data.local.bookmarks

import androidx.room.*
import com.krendel.neusfeet.data.articles.Article
import io.reactivex.Single

@Dao
interface BookmarksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(article: Article): Long

    @Delete
    fun delete(article: Article): Int

    // TODO add pagination
    @Query("SELECT * FROM article ORDER BY localId DESC")
    fun all(): Single<Article>

}