package com.twenty2byte.simplenews.vm

import androidx.lifecycle.*
import com.twenty2byte.simplenews.repository.RoomRepository
import com.twenty2byte.simplenews.source.local.NewsEntity
import com.twenty2byte.simplenews.source.remote.news.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(private val repository: RoomRepository): ViewModel() {
    var newsData: MutableLiveData<MutableList<NewsEntity?>> = MutableLiveData()
    var favoriteNewsData: MutableLiveData<MutableList<NewsEntity?>> = MutableLiveData()
    val favoriteList = mutableListOf<NewsEntity?>()


    fun setNewsToDb(newsResponse: MutableList<Article?>) = viewModelScope.launch(Dispatchers.IO) {
        val list = mutableListOf<NewsEntity>()
            newsResponse.forEach {
                list.add(NewsEntity(0, it?.author.toString(),
                    it?.content, it?.description, it?.publishedAt, it?.source?.name, it?.title, it?.url,
                    it?.urlToImage, 0, "ru"))
        }
        repository.insertNewsList(list)
        getAllNewsFromDb()
    }

    fun getAllNewsObservers(): MutableLiveData<MutableList<NewsEntity?>>{
        getAllNewsFromDb()
        return newsData
    }

    fun getAllFavoriteNewsObservers(): MutableLiveData<MutableList<NewsEntity?>>{
        getFavoritesNews()
        return favoriteNewsData
    }

    fun isDbEmpty(): Boolean = repository.getAllNews()?.size == 0

    private fun getFavoritesNews() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllNews()?.forEach {
            if (it?.isFavorite == 1) favoriteList.add(it)
        }
        favoriteNewsData.postValue(favoriteList)
    }

    private fun getAllNewsFromDb() {
         val list = repository.getAllNews()
        newsData.postValue(list!!)
    }

    private fun insertNews(newsEntity: NewsEntity) = viewModelScope.launch {
        repository?.insert(newsEntity)
        getAllNewsFromDb()
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