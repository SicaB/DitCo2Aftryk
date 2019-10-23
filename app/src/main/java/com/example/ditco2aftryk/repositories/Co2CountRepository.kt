package com.example.ditco2aftryk.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ditco2aftryk.model.AppDatabase
import com.example.ditco2aftryk.model.entities.Co2Count


class Co2CountRepository(private val database: AppDatabase) {

    // Observed LiveData will notify the observer when the data has changed
    val co2Count: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

//    fun calculateFlightCo2(co2Count: String) : LiveData<String>{
//
//        // The response from the database is stored inside this value
//        val co2CountResponse = MutableLiveData<String>()
//
//    }

    // Function to save the co2 count in the database
    suspend fun saveCo2Count(co2Count: Co2Count) = database.getCo2CountDao().insert(co2Count)

    fun getCo2Count() = database.getCo2CountDao().getCo2Count()

}