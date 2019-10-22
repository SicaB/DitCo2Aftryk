package com.example.ditco2aftryk.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ditco2aftryk.repositories.Co2CountRepository

@Suppress("UNCHECKED_CAST")
class HomeScreenViewModelFactory(private val repository: Co2CountRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(repository) as T
    }

}