package com.example.ditco2aftryk.view.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.Service
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ditco2aftryk.model.AppDatabase
import com.example.ditco2aftryk.model.entities.Co2Count
import com.example.ditco2aftryk.model.entities.DailyCo2Count
import com.example.ditco2aftryk.model.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.Listener
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application){

    var listener: Listener? = null

    // ViewModel maintains a reference to the repository to get data.
    private val repository: Co2CountRepository
    // LiveData is implemented to notify the activity for changes to accumulatedCo2Count
    val accumulatedCo2Count: LiveData<String>
    val dailyCo2Count: LiveData<String>


    init {
        // Gets reference to getCo2CountDao from AppDatabase to construct
        // the correct Co2CountRepository.
        val co2CountDao = AppDatabase.invoke(application).getCo2CountDao()
        val dailyCo2CountDao = AppDatabase.invoke(application).getDailyCo2CountDao()
        repository = Co2CountRepository(co2CountDao, dailyCo2CountDao)
        accumulatedCo2Count = repository.accumulatedCo2Counts
        dailyCo2Count = repository.dailySavedCo2Count
    }

    // Function to delete all user input for the day
    fun deleteCountDay(){
        repository.deleteAllCo2CountsFromTable()
    }

    // Function to delete all user input for the day
    fun deleteCountWeek(){
        repository.deleteAllDailyInsertedCo2Counts()
    }

    // Function to insert user input using a coroutine
    fun insertDailyCount() = viewModelScope.launch {
        val input = DailyCo2Count(0, accumulatedCo2Count.value!!)
        Log.d("MyTag", "Inserted")
        repository.saveDailyCo2Count(input)
        repository.deleteAllCo2CountsFromTable()
    }

}














