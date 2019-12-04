package com.example.ditco2aftryk.view.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ditco2aftryk.model.AppDatabase
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
    val changesToWeeklyCounts: LiveData<List<String>>
    val dateInCo2CountTable: String

    init {
        // Gets reference to getCo2CountDao from AppDatabase to construct
        // the correct Co2CountRepository.
        val co2CountDao = AppDatabase.invoke(application).getCo2CountDao()
        val dailyCo2CountDao = AppDatabase.invoke(application).getDailyCo2CountDao()
        repository = Co2CountRepository(co2CountDao, dailyCo2CountDao)
        accumulatedCo2Count = repository.accumulatedCo2Counts
        dateInCo2CountTable = repository.dateInCo2CountTable
        changesToWeeklyCounts = repository.changesToWeeklyCounts
    }

    // Function to delete all user input for the day
    fun deleteCountDay(){
        repository.deleteAllCo2CountsFromTable()
    }

    // Function to delete all user input for the day
    fun deleteCountWeek(){
        repository.deleteAllDailyInsertedCo2Counts()
    }

    // Function to delete inputs in database older than one week
    fun deleteInputsOlderThanOneWeek(){
        repository.deleteInputsOlderThanOneWeek()
    }

    // Function to insert daily accumulated count into weekly table using a coroutine
    fun insertDailyCount(dailyCo2Count: DailyCo2Count) = viewModelScope.launch {
        repository.saveDailyCo2Count(dailyCo2Count)
    }

    // Function to insert empty values in database if no value is present
    fun insertEmptyValuesIntoWeekTable() = viewModelScope.launch{
        repository.insertEmptyValuesIntoWeekTable()
    }

}














