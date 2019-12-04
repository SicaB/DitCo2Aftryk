package com.example.ditco2aftryk.model

import android.content.Context
import androidx.room.*
import com.example.ditco2aftryk.model.entities.Co2Count
import com.example.ditco2aftryk.model.entities.DailyCo2Count

@Database(entities = [Co2Count::class, DailyCo2Count::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    // In the database we have to implement abstract functions for all dao's in the project
    abstract fun getCo2CountDao() : Co2CountDao
    abstract fun getDailyCo2CountDao() : DailyCo2CountDao

    // Create AppDatabase Singleton
    companion object{
        // Singleton prevents multible instances of database opening at the same time
        // Volatile makes this variable visible to all other threads
        @Volatile
        private var INSTANCE: AppDatabase? = null
        // Lock value is created to make sure we do not create two instances of the database
        private val LOCK = Any()

        // If INSTANCE is not null return INSTANCE else create synchronized block
        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK){
            // If INSTANCE is null call buildDatabase function
            INSTANCE?:buildDatabase(context).also {
                INSTANCE = it
            }
        }

        // Build the database
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "MyDatabase.db"
            )
                .allowMainThreadQueries()
                .build()
    }

}




//fun getDatabase(context: Context): AppDatabase {
//    val tempInstance = AppDatabase.INSTANCE
//    if (tempInstance != null) {
//        return tempInstance
//    }
//    synchronized(this) {
//        val instance = Room.databaseBuilder(
//            context.applicationContext,
//            AppDatabase::class.java,
//            "my_database"
//        )
//            .build()
//        AppDatabase.INSTANCE = instance
//        return instance
//    }
//}