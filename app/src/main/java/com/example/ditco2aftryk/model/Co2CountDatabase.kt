package com.example.ditco2aftryk.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Co2Count::class], version = 1)
abstract class Co2CountDatabase : RoomDatabase(){


    // Singleton prevents multible instances of database opening at the same time

}