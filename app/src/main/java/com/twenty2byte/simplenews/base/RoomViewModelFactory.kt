package com.twenty2byte.simplenews.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twenty2byte.simplenews.repository.RoomRepository
import com.twenty2byte.simplenews.vm.RoomViewModel

@Suppress("UNCHECKED_CAST")
class RoomViewModelFactory(private val repository: RoomRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            return RoomViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}

