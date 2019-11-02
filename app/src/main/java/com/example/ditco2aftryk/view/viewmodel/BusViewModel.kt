package com.example.ditco2aftryk.view.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ditco2aftryk.model.AppDatabase
import com.example.ditco2aftryk.model.entities.Co2Count
import com.example.ditco2aftryk.model.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.Listener
import kotlinx.coroutines.launch

class BusViewModel (application: Application) : AndroidViewModel(application) {

    // ViewModel maintains a reference to the repository to get data.
    private val repository: Co2CountRepository

    var listener: Listener? = null

    val busCo2Input = MutableLiveData<String>()

    private lateinit var input: Co2Count

    init {
        // Gets reference to getCo2CountDao from AppDatabase to construct
        // the correct Co2CountRepository.
        val co2CountDao = AppDatabase.invoke(application).getCo2CountDao()
        repository = Co2CountRepository(co2CountDao)
    }

    // Calculation of bus co2 based on input
    fun calculateBusCo2(input: String) : String{
        val busCo2InKg = input.toInt() * 92
        return busCo2InKg.toString()
    }

    // Function to save user input in the database when button is clicked
    fun onSaveCo2ButtonClick(@Suppress("UNUSED_PARAMETER")view: View){
        if(!busCo2Input.value?.isDigitsOnly()!! || busCo2Input.value.isNullOrEmpty()){
            listener?.onFailure("Invalid co2 value entered.")
            return
        }

        input = Co2Count(0, calculateBusCo2(busCo2Input.value!!))
        insert(input)
        listener?.onSuccess()
    }

    // Function to insert user input using a coroutine
    fun insert(co2Count: Co2Count) = viewModelScope.launch {
        Log.d("MyTag", "Inserted")
        repository.saveCo2Count(co2Count)
    }

}