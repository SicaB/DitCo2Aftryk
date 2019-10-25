package com.example.ditco2aftryk.view.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ditco2aftryk.model.AppDatabase
import com.example.ditco2aftryk.model.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.Listener

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    // ViewModel maintains a reference to the repository to get data.
    private val repository: Co2CountRepository
    // LiveData is implemented to notify the activity for changes to accumulatedCo2Count
    val accumulatedCo2Count: LiveData<String>

    init {
        // Gets reference to getCo2CountDao from AppDatabase to construct
        // the correct Co3CountRepository.
        val co2CountDao = AppDatabase.invoke(application).getCo2CountDao()
        repository = Co2CountRepository(co2CountDao)
        accumulatedCo2Count = repository.accumulatedCo2Counts
    }

    var listener: Listener? = null

}



