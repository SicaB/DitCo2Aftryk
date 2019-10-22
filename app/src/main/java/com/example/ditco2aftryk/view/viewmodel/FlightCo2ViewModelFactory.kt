package com.example.ditco2aftryk.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ditco2aftryk.repositories.Co2CountRepository

class FlightCo2ViewModelFactory(private val repository: Co2CountRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FlightCo2ViewModel(repository) as T
    }

}