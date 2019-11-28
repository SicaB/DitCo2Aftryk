package com.example.ditco2aftryk.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ditco2aftryk.model.entities.DailyCo2Count

// This is where the database CRUD (create, read, update and delete) operations are defined.

@Dao
interface DailyCo2CountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dailyCo2Count: DailyCo2Count)

    @Query("DELETE FROM daily_co2_counts")
    fun deleteAllDailyInsertedCo2Counts()

    //This query takes the very first saved data to daily_co2_count table
    //Which is yesterdays count.
    @Query("SELECT dailyCo2Size FROM daily_co2_counts order by id DESC limit 1")
    fun getDailySavedCo2Count() : LiveData<String>
}