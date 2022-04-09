package com.twenty2byte.simplenews.source.remote.news

data class Article(
    val author: Any?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
)