package com.twenty2byte.simplenews.source.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RestApiRequests {

    @GET("/v2/top-headlines")
    suspend fun getNewsFromApi(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String): NewsResponse
}