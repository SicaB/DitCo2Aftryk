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

class MeatViewModel(application: Application) : AndroidViewModel(application) {

    // ViewModel maintains a reference to the repository to get data.
    private val repository: Co2CountRepository
    var listener: Listener? = null
    val meatCo2Input = MutableLiveData<String>()
    var meatCo2 = ""
    private lateinit var input: Co2Count

    init {
        // Gets reference to getCo2CountDao from AppDatabase to construct
        // the correct Co2CountRepository.
        val co2CountDao = AppDatabase.invoke(application).getCo2CountDao()
        val dailyCo2CountDao = AppDatabase.invoke(application).getDailyCo2CountDao()
        repository = Co2CountRepository(co2CountDao, dailyCo2CountDao)
    }

    // Function to get the correct co2 in gram pr gram meat cooked based on cartype
    private fun getCo2BasedOnMeatType(carTypeInput: String?) {
        when (carTypeInput) {
            "Okse" -> meatCo2 = calculateMeatCo2(meatCo2Input.value!!, 19.4)
            "Svin" -> meatCo2 = calculateMeatCo2(meatCo2Input.value!!, 3.6)
            "Kylling" -> meatCo2 = calculateMeatCo2(meatCo2Input.value!!, 3.4)
            "Lam" -> meatCo2 = calculateMeatCo2(meatCo2Input.value!!, 14.5)
        }
    }

    // Calculation of co2 based on input
    private fun calculateMeatCo2(input: String, meatTypeInputValue: Double) : String{
        val meatCo2InGram = input.toDouble() * meatTypeInputValue
        return meatCo2InGram.toString()
    }

    // Function to save user input in the database when button is clicked
    fun onSaveCo2ButtonClick(meatType: String){

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())

        if(meatCo2Input.value.isNullOrEmpty()){
            listener?.onFailure("Indtast mængde kød tilberedt.")
            return
        }

        getCo2BasedOnMeatType(meatType)
        input = Co2Count(0, meatCo2, currentDate)
        insert(input)
        listener?.onSuccess()
    }

    // Function to insert user input using a coroutine
    fun insert(co2Count: Co2Count) = viewModelScope.launch {
        Log.d("MyTag", "Inserted")
        repository.saveCo2Count(co2Count)
    }
}