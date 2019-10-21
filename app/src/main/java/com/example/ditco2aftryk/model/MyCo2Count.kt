package com.example.ditco2aftryk.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class MyCo2Count(@PrimaryKey(autoGenerate=true) // generates id: 0, 1, 2, 3 ect
                      var id: Int,
                      var co2Size: Int) {

    override fun toString(): String {
        return "MyCo2Count(size = $co2Size)"
    }
}

@Dao
// Here we can define a DAO as a public interface
public interface MyCo2CountDao {
    // Selects everything in the table
    @Query("select * from MyCo2Count")
    fun fetchAll(): LiveData<List<MyCo2Count>>

    @Insert
    suspend fun insert(arg: MyCo2Count)

    @Update
    suspend fun update(arg: MyCo2Count)

    @Delete
    suspend fun delete(arg: MyCo2Count)

}