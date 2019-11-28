package com.example.ditco2aftryk.model.repositories

import androidx.lifecycle.LiveData
import com.example.ditco2aftryk.model.Co2CountDao
import com.example.ditco2aftryk.model.DailyCo2CountDao
import com.example.ditco2aftryk.model.entities.Co2Count
import com.example.ditco2aftryk.model.entities.DailyCo2Count

open class Co2CountRepository (
    private val co2CountDao: Co2CountDao,
    private val dailyCo2CountDao: DailyCo2CountDao
) {

    // Observed LiveData will notify the observer when the data has changed
    val accumulatedCo2Counts: LiveData<String> = co2CountDao.getAccumulatedCo2Counts()
    val dailySavedCo2Count: LiveData<String> = dailyCo2CountDao.getDailySavedCo2Count()

    // Function to save the co2 count in the database
    suspend fun saveCo2Count(co2Count: Co2Count) {
        co2CountDao.insert(co2Count)
    }

    // Function to delete co2_counts table
    fun deleteAllCo2CountsFromTable(){
        co2CountDao.deleteAllCo2CountsFromTable()
    }

    // Function to delete daily_co2_counts table
    fun deleteAllDailyInsertedCo2Counts(){
        dailyCo2CountDao.deleteAllDailyInsertedCo2Counts()

    }

    // Function to save the daily co2 count in the database
    suspend fun saveDailyCo2Count(dailyCo2Count: DailyCo2Count) {
        dailyCo2CountDao.insert(dailyCo2Count)
    }

}

