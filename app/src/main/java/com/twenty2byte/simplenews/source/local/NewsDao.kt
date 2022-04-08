package com.twenty2byte.simplenews.source.local

import androidx.room.*

@Dao
interface NewsDao {

    @Query("SELECT * FROM news ORDER by id DESC")
    fun getAllNews():MutableList<NewsEntity>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNews(newsEntity: NewsEntity?)

    @Delete
    fun deleteNews(newsEntity: NewsEntity?)

    @Update
    fun updateNews(newsEntity: NewsEntity?)
}