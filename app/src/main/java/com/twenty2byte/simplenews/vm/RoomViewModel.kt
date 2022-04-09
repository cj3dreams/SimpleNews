package com.twenty2byte.simplenews.vm

import android.app.Application
import androidx.lifecycle.*
import com.twenty2byte.simplenews.repository.RoomRepository
import com.twenty2byte.simplenews.source.local.NewsEntity
import com.twenty2byte.simplenews.source.local.RoomAppDb
import com.twenty2byte.simplenews.source.remote.NewsResponse
import com.twenty2byte.simplenews.source.remote.Resource
import com.twenty2byte.simplenews.source.remote.news.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(private val repository: RoomRepository): ViewModel() {
    var newsData: MutableLiveData<MutableList<NewsEntity?>> = MutableLiveData()

    fun setNewsToDb(newsResponse: MutableList<Article?>) = viewModelScope.launch {
            newsResponse.forEach{
                insertNews((NewsEntity(0, it?.author.toString(),
                    it?.content, it?.description, it?.publishedAt, it?.source?.name, it?.title, it?.url,
                    it?.urlToImage, 0, "ru")))
            }
        getAllNewsFromDb()
    }

    fun getAllNewsObservers(): MutableLiveData<MutableList<NewsEntity?>>{
        getAllNewsFromDb()
        return newsData
    }

    fun getAllNewsFromDb() {
         val list = repository.getAllNews()
        newsData.postValue(list!!)
    }

    private fun insertNews(newsEntity: NewsEntity) = viewModelScope.launch {
        repository?.insert(newsEntity)
    }
    fun deleteNews(newsEntity: NewsEntity) = viewModelScope.launch {
        repository?.delete(newsEntity)
        getAllNewsFromDb()
    }
    fun updateNews(newsEntity: NewsEntity) = viewModelScope.launch {
        repository?.update(newsEntity)
        getAllNewsFromDb()
    }
    fun deleteAll() = viewModelScope.launch {
        repository?.deleteAll()
        getAllNewsFromDb()
    }
}