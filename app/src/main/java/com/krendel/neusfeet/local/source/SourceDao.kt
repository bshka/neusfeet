package com.krendel.neusfeet.local.source

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface SourceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(source: Source)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(source: Source)

    @Delete
    fun remove(source: Source)

    @Query("SELECT * FROM source ORDER BY id")
    fun all(): Flowable<List<Source>>

    @Query("SELECT group_concat(id) FROM source")
    fun sources(): Flowable<String>

}