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

    @Query("DELETE FROM daily_co2_counts WHERE date < datetime('now', '-1 week')")
    fun deleteInputsOlderThanOneWeek()

    @Query("SELECT dailyCo2Size FROM daily_co2_counts")
    fun getChangesToWeeklyCounts() : LiveData<List<String>>

    @Query("""
        INSERT OR IGNORE INTO daily_co2_counts(id, dailyCo2Size, date) 
        VALUES
        ('1', '0', '31/12/9999'),
        ('2', '0', '31/12/9999'),
        ('3', '0', '31/12/9999'),
        ('4', '0', '31/12/9999'),
        ('5', '0', '31/12/9999'),
        ('6', '0', '31/12/9999'),
        ('7', '0', '31/12/9999')
    """)
    fun insertEmptyValuesIntoWeekTable()

}