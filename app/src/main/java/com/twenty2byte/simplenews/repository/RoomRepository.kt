package com.twenty2byte.simplenews.repository

import com.twenty2byte.simplenews.source.local.NewsDao
import com.twenty2byte.simplenews.source.local.NewsEntity

class RoomRepository(private val newsDao: NewsDao) {

    fun getAllNews() = newsDao.getAllNews()
    suspend fun update(newsEntity: NewsEntity) = newsDao.updateNews(newsEntity)
    suspend fun deleteAll() = newsDao.deleteAllNews()
    suspend fun insertNewsList(newsEntityList: MutableList<NewsEntity>) = newsDao.insertNewsList(newsEntityList)

}