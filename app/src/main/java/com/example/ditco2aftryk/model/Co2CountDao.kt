package com.example.ditco2aftryk.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ditco2aftryk.model.entities.Co2Count

// This is where the database CRUD (create, read, update and delete) operations are defined.

@Dao
interface Co2CountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(co2Count: Co2Count)

    @Query("SELECT SUM(co2Size) FROM co2_counts")
    fun getAccumulatedCo2Counts() : LiveData<String>

}