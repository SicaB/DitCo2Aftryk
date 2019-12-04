package com.example.ditco2aftryk.view.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.ditco2aftryk.model.AppDatabase
import com.example.ditco2aftryk.model.entities.Co2Count
import com.example.ditco2aftryk.model.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.Listener
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CarViewModel (application: Application) : AndroidViewModel(application) {

    // ViewModel maintains a reference to the repository to get data.
    private val repository: Co2CountRepository
    var listener: Listener? = null
    val carCo2Input = MutableLiveData<String>()
    var carCo2 = ""

    private lateinit var input: Co2Count

    init {
        // Gets reference to getCo2CountDao from AppDatabase to construct
        // the correct Co2CountRepository.
        val co2CountDao = AppDatabase.invoke(application).getCo2CountDao()
        val dailyCo2CountDao = AppDatabase.invoke(application).getDailyCo2CountDao()
        repository = Co2CountRepository(co2CountDao, dailyCo2CountDao)
    }

    // Function to get the correct co2 in gram pr km based on cartype
    private fun getCo2BasedOnCarType(carTypeInput: String?) {
        when (carTypeInput) {
            "Lille bil" -> carCo2 = calculateCarCo2(carCo2Input.value!!, 110.0)
            "Mellemstor bil" -> carCo2 = calculateCarCo2(carCo2Input.value!!, 133.0)
            "Stor bil" -> carCo2 = calculateCarCo2(carCo2Input.value!!, 183.0)
            "Diesel bil" -> carCo2 = calculateCarCo2(carCo2Input.value!!, 160.0)
            "Hybrid bil" -> carCo2 = calculateCarCo2(carCo2Input.value!!, 84.0)
            "El-bil" -> carCo2 = calculateCarCo2(carCo2Input.value!!, 43.0)
        }
    }

    // Calculation of co2 based on input
    private fun calculateCarCo2(input: String, carTypeInputValue: Double) : String{
        val carCo2InGram = input.toDouble() * carTypeInputValue
        return carCo2InGram.toString()
    }

    // Function to save user input in the database when button is clicked
    fun onSaveCo2ButtonClick(carType: String){

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        if(carCo2Input.value.isNullOrEmpty()){
            listener?.onFailure("Indtast antal km kørt.")
            return
        }

        getCo2BasedOnCarType(carType)
        input = Co2Count(0, carCo2, currentDate)
        insert(input)
        listener?.onSuccess()
    }

    // Function to insert user input using a coroutine
    fun insert(co2Count: Co2Count) = viewModelScope.launch {
        Log.d("MyTag", "Inserted")
        repository.saveCo2Count(co2Count)
    }

}