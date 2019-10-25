package com.example.ditco2aftryk.model.repositories

import androidx.lifecycle.LiveData
import com.example.ditco2aftryk.model.Co2CountDao
import com.example.ditco2aftryk.model.entities.Co2Count


class Co2CountRepository(private val co2CountDao: Co2CountDao) {

    // Observed LiveData will notify the observer when the data has changed
    val accumulatedCo2Counts: LiveData<String> = co2CountDao.getAccumulatedCo2Counts()

    // Function to save the co2 count in the database
    suspend fun saveCo2Count(co2Count: Co2Count) {
        co2CountDao.insert(co2Count)
    }
}