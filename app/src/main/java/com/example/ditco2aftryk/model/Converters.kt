package com.example.ditco2aftryk.model

import androidx.room.TypeConverter
import java.util.*

object Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

//    @TypeConverter
//    @JvmStatic
//    fun fromTimestamp(value: Long?): Calendar? = value?.let { value ->
//        GregorianCalendar().also { calendar ->
//            calendar.timeInMillis = value
//        }
//    }
//
//    @TypeConverter
//    @JvmStatic
//    fun toTimestamp(timestamp: Calendar?): Long? = timestamp?.timeInMillis
}