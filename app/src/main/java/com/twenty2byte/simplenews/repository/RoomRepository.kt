package com.twenty2byte.simplenews.repository

import android.app.Application
import android.content.Context
import com.twenty2byte.simplenews.source.local.NewsEntity
import com.twenty2byte.simplenews.source.local.RoomAppDb

class RoomRepository(context: Context) {

    private val newsDao = RoomAppDb.getAppDb(context)?.newsDao()

    fun getAllNews() = newsDao?.getAllNews()
    suspend fun insert(newsEntity: NewsEntity) = newsDao?.insertNews(newsEntity)
    suspend fun delete(newsEntity: NewsEntity) = newsDao?.deleteNews(newsEntity)
    suspend fun update(newsEntity: NewsEntity) = newsDao?.updateNews(newsEntity)
    suspend fun deleteAll() = newsDao?.deleteAllNews()

}