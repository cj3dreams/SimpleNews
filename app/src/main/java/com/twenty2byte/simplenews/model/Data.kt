package com.twenty2byte.simplenews.model

data class Data(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)