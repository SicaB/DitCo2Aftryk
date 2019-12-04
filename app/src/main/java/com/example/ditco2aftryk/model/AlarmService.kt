package com.example.ditco2aftryk.model

import android.annotation.SuppressLint
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
import java.text.SimpleDateFormat
import java.util.*


class AlarmService : Service() {

        val context = HomeScreenActivity.appContext
        private var cal = Calendar.getInstance()
        private val co2CountDao = AppDatabase.invoke(context).getCo2CountDao()
        private val dailyCo2CountDao = AppDatabase.invoke(context).getDailyCo2CountDao()
        private val repository = Co2CountRepository(co2CountDao, dailyCo2CountDao)
        private lateinit var input: DailyCo2Count

        override fun onBind(intent: Intent?): IBinder? {
            return null
        }

        @SuppressLint("SimpleDateFormat")
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
            val dailyCo2Count: String? = co2CountDao.getAccumulatedCo2CountsAsString()
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())

                when (cal.get(Calendar.DAY_OF_WEEK)){
                    Calendar.MONDAY -> input = DailyCo2Count(1, dailyCo2Count.toString(), currentDate)
                    Calendar.TUESDAY -> input = DailyCo2Count(2, dailyCo2Count.toString(), currentDate)
                    Calendar.WEDNESDAY -> input = DailyCo2Count(3, dailyCo2Count.toString(), currentDate)
                    Calendar.THURSDAY -> input = DailyCo2Count(4, dailyCo2Count.toString(), currentDate)
                    Calendar.FRIDAY -> input = DailyCo2Count(5, dailyCo2Count.toString(), currentDate)
                    Calendar.SATURDAY -> input = DailyCo2Count(6, dailyCo2Count.toString(), currentDate)
                    Calendar.SUNDAY -> input = DailyCo2Count(7, dailyCo2Count.toString(), currentDate)
                }

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