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
    val dateInCo2CountTable: String = co2CountDao.getDateInTable()
    val changesToWeeklyCounts: LiveData<List<String>> = dailyCo2CountDao.getChangesToWeeklyCounts()

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

    // Function to delete inputs in database older than one week
    fun deleteInputsOlderThanOneWeek(){
        dailyCo2CountDao.deleteInputsOlderThanOneWeek()
    }

    // Function to save the daily co2 count in the database
    suspend fun saveDailyCo2Count(dailyCo2Count: DailyCo2Count) {
        dailyCo2CountDao.insert(dailyCo2Count)
    }

    // Function to insert empty values in database if no value is present
    fun insertEmptyValuesIntoWeekTable(){
        dailyCo2CountDao.insertEmptyValuesIntoWeekTable()
    }

}

