package com.twenty2byte.simplenews.source.local

import androidx.room.*

@Dao
interface NewsDao {

    @Query("SELECT * FROM news ORDER by id DESC")
    fun getAllNews(): MutableList<NewsEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewsList(newsEntity: MutableList<NewsEntity>?)

    @Query("DELETE FROM news")
    suspend fun deleteAllNews()

    @Update
    suspend fun updateNews(newsEntity: NewsEntity?)
}