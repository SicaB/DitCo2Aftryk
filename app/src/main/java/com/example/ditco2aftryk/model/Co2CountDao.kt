package com.example.ditco2aftryk.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ditco2aftryk.model.entities.Co2Count

@Dao
interface Co2CountDao {

    @Insert
    suspend fun insert(co2Count: Co2Count) : Long // Long to get the raw id when co2Count is successfully inserted

    @Query("SELECT * FROM co2Count")
    fun getCo2Count() : LiveData<Co2Count>
}