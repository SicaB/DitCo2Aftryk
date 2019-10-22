package com.example.ditco2aftryk.model

import androidx.room.*

@Entity
data class Co2Count(@PrimaryKey(autoGenerate=true) // generates id: 0, 1, 2, 3 ect
                      var id: Int,
                      var co2Size: Int) {

    override fun toString(): String {
        return "Co2Count(size = $co2Size)"
    }
}
