package com.example.ditco2aftryk.model.entities

import androidx.room.*

@Entity(tableName = "co2_counts")
data class Co2Count(@PrimaryKey(autoGenerate=true) // generates id: 0, 1, 2, 3 ect
                        var id: Int,
                        var co2Size: String) {

    override fun toString(): String {
        return "Co2Count (size = $co2Size)"
    }
}
