package com.example.ditco2aftryk.model.entities

import androidx.room.*

@Entity(tableName = "daily_co2_counts")
data class DailyCo2Count(@PrimaryKey(autoGenerate = false)
                            var id: Int,
                            var dailyCo2Size: String,
                            var date: String)