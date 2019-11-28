package com.example.ditco2aftryk.view.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ditco2aftryk.model.AppDatabase
import com.example.ditco2aftryk.model.entities.Co2Count
import com.example.ditco2aftryk.model.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.Listener
import kotlinx.coroutines.launch

class HeatingViewModel (application: Application) : AndroidViewModel(application) {

    // ViewModel maintains a reference to the repository to get data.
    private val repository: Co2CountRepository

    var listener: Listener? = null
    val heatingCo2Input = MutableLiveData<String>()
    var heatingCo2 = ""

    private lateinit var input: Co2Count

    init {
        // Gets reference to getCo2CountDao from AppDatabase to construct
        // the correct Co2CountRepository.
        val co2CountDao = AppDatabase.invoke(application).getCo2CountDao()
        val dailyCo2CountDao = AppDatabase.invoke(application).getDailyCo2CountDao()
        repository = Co2CountRepository(co2CountDao, dailyCo2CountDao)
    }

    // Function to get the correct co2 in gram pr kWh based on heatingtype
    fun getCo2BasedOnHeatingType(heatingTypeInput: String?) {
        when (heatingTypeInput) {
            "Fjernvarme" -> heatingCo2 = calculateHeatingCo2(heatingCo2Input.value!!, 80.0)
            "Bygas" -> heatingCo2 = calculateHeatingCo2(heatingCo2Input.value!!, 142.0)
            "Fjernkøling" -> heatingCo2 = calculateHeatingCo2(heatingCo2Input.value!!, 41.0)
        }
    }

    // Calculation of co2 based on input
    fun calculateHeatingCo2(input: String, heatingTypeInput: Double) : String{
        val heatingCo2InGram = input.toDouble() * heatingTypeInput
        return heatingCo2InGram.toString()
    }

    // Function to save user input in the database when button is clicked
    fun onSaveCo2ButtonClick(heatingType: String){
        if(heatingCo2Input.value.isNullOrEmpty()){
            listener?.onFailure("Indtast antal kWh.")
            return
        }

        getCo2BasedOnHeatingType(heatingType)
        input = Co2Count(0, heatingCo2)
        insert(input)
        listener?.onSuccess()
    }

    // Function to insert user input using a coroutine
    fun insert(co2Count: Co2Count) = viewModelScope.launch {
        Log.d("MyTag", "Inserted")
        repository.saveCo2Count(co2Count)
    }

}