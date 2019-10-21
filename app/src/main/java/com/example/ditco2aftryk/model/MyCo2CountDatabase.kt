package com.example.ditco2aftryk.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyCo2Count::class], version = 1)
abstract class MyCo2CountDatabase : RoomDatabase(){

    // define dataAccesObject
    abstract fun myCo2CountDao() : MyCo2CountDao

    // Singleton prevents multible instances of database opening at the same time
    companion object {
        @Volatile
        private var INSTANCE: MyCo2CountDatabase? = null

        fun getDatabase(context: Context): MyCo2CountDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyCo2CountDatabase::class.java,
                    "MyCo2CountDatabase"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}