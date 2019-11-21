package com.example.ditco2aftryk.model.repositories

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
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

    }

    // Function to save the daily co2 count in the database
    suspend fun saveDailyCo2Count(dailyCo2Count: DailyCo2Count) {
        dailyCo2CountDao.insert(dailyCo2Count)
    }

    class AlarmService : Service(){

        override fun onBind(intent: Intent?): IBinder? {
            return null

        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            Log.d("alarmmanager", "Alarm")

            return super.onStartCommand(intent, flags, startId)
        }
    }
}

