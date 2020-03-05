package com.example.alarmapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Alarm::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}