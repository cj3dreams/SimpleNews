package com.twenty2byte.simplenews.source.remote

import com.twenty2byte.simplenews.source.remote.news.Article

data class NewsResponse(
    val articles: MutableList<Article?>,
    val status: String,
    val totalResults: Int
)