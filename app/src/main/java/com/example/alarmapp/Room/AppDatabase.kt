package com.example.alarmapp.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.alarmapp.Room.Alarm
import com.example.alarmapp.Room.AlarmDao

@Database(entities = arrayOf(Alarm::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao
}