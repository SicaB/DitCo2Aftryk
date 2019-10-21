package com.example.ditco2aftryk.repositories

import androidx.lifecycle.LiveData
import com.example.ditco2aftryk.model.MyCo2Count
import com.example.ditco2aftryk.model.MyCo2CountDao

class Co2CountRepository(private val myCo2CountDao: MyCo2CountDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed
    val allCo2Counts: LiveData<List<MyCo2Count>> = myCo2CountDao.fetchAll()

    // The suspend motifier tells the compiler that this must be called from a coroutine or another suspend function.
    suspend fun insert(arg: MyCo2Count) {
        myCo2CountDao.insert(arg)
    }

    suspend fun update(arg: MyCo2Count) {
        myCo2CountDao.update(arg)
    }

    suspend fun delete(arg: MyCo2Count) {
        myCo2CountDao.delete(arg)
    }

}