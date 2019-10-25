package com.example.ditco2aftryk.view.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.*
import com.example.ditco2aftryk.model.AppDatabase
import com.example.ditco2aftryk.model.entities.Co2Count
import com.example.ditco2aftryk.model.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.Listener
import kotlinx.coroutines.launch

class FlightCo2ViewModel(application: Application) : AndroidViewModel(application){

    // ViewModel maintains a reference to the repository to get data.
    private val repository: Co2CountRepository
    val accumulatedCo2Count: LiveData<String>

    var flightCo2Input: String? = null

    var listener: Listener? = null

    private lateinit var input: Co2Count

    init {
        // Gets reference to getCo2CountDao from AppDatabase to construct
        // the correct Co2CountRepository.
        val co2CountDao = AppDatabase.invoke(application).getCo2CountDao()
        repository = Co2CountRepository(co2CountDao)
        accumulatedCo2Count = repository.accumulatedCo2Counts
    }

    // Function to save users input in the database
    fun onSaveCo2ButtonClick(@Suppress("UNUSED_PARAMETER")view: View){
        if(!flightCo2Input?.isDigitsOnly()!! || flightCo2Input.isNullOrEmpty()){
            listener?.onFailure("Invalid co2 value entered.")
            return
        }
        input = Co2Count(0, flightCo2Input!!)
        insert(input)
        listener?.onSuccess()
    }

    fun insert(co2Count: Co2Count) = viewModelScope.launch {
        Log.d("MyTag", "Inserted")
        repository.saveCo2Count(co2Count)
    }
}