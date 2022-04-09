package com.twenty2byte.simplenews.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twenty2byte.simplenews.repository.NewsRepository
import com.twenty2byte.simplenews.source.remote.NewsResponse
import com.twenty2byte.simplenews.source.remote.Resource
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository): ViewModel() {

    private val _newsResponse: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val newsResponse: LiveData<Resource<NewsResponse>> get() = _newsResponse

    fun getNews(country: String, apiKey: String) = viewModelScope.launch {
        _newsResponse.value = repository.getNews(country, apiKey)
    }
}