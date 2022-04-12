package com.twenty2byte.simplenews.repository

import android.content.Context
import com.twenty2byte.simplenews.source.local.NewsEntity
import com.twenty2byte.simplenews.source.local.RoomAppDb

class RoomRepository(context: Context) {

    private val newsDao = RoomAppDb.getAppDb(context)?.newsDao()

    fun getAllNews() = newsDao?.getAllNews()
    suspend fun update(newsEntity: NewsEntity) = newsDao?.updateNews(newsEntity)
    suspend fun deleteAll() = newsDao?.deleteAllNews()
    suspend fun insertNewsList(newsEntityList: MutableList<NewsEntity>) = newsDao?.insertNewsList(newsEntityList)

}