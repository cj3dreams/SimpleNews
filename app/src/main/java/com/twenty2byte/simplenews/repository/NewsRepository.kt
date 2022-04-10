package com.twenty2byte.simplenews.repository

import com.twenty2byte.simplenews.base.BaseRepository
import com.twenty2byte.simplenews.source.remote.RestApiRequests

class NewsRepository(private val api: RestApiRequests): BaseRepository() {

    suspend fun getNews(country: String, apiKey: String) = safeApiCall {
        api.getNewsFromApi(country, apiKey)
    }
}