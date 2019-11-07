package com.example.ditco2aftryk.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_co2_counts")
data class DailyCo2Count(@PrimaryKey(autoGenerate = true)
                            var id: Int,
                            var dailyCo2Size: String) {

}