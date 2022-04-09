package com.twenty2byte.simplenews.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twenty2byte.simplenews.repository.NewsRepository
import com.twenty2byte.simplenews.vm.NewsViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: BaseRepository)
    :ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(NewsViewModel::class.java) ->
                NewsViewModel(repository as NewsRepository) as T

            else -> throw IllegalAccessException("ViewModel Class Not Found")
        }
    }
}