package com.example.ditco2aftryk.model

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.ditco2aftryk.model.entities.DailyCo2Count
import com.example.ditco2aftryk.model.repositories.Co2CountRepository
import com.example.ditco2aftryk.view.ui.HomeScreenActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


class AlarmService : Service() {

        val context = HomeScreenActivity.appContext
        private val co2CountDao = AppDatabase.invoke(context).getCo2CountDao()
        private val dailyCo2CountDao = AppDatabase.invoke(context).getDailyCo2CountDao()
        private val repository = Co2CountRepository(co2CountDao, dailyCo2CountDao)

        private lateinit var input: DailyCo2Count

        override fun onBind(intent: Intent?): IBinder? {
            return null

        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            val dailyCo2Count = co2CountDao.getAccumulatedCo2CountsAsString()
            input = DailyCo2Count(0, dailyCo2Count)
            insert(input)
            co2CountDao.deleteAllCo2CountsFromTable()
            Log.d("alarmmanager", "Alarm ${co2CountDao.getAccumulatedCo2CountsAsString()}")

            return super.onStartCommand(intent, flags, startId)
        }

        // Function to insert user input using a coroutine
        fun insert(dailyCo2Count: DailyCo2Count) = CoroutineScope(Main).launch {
            repository.saveDailyCo2Count(dailyCo2Count)
            Log.d("MyTag", "DailyCo2Count Inserted")
        }


}