package com.example.alarmapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Alarm::class), version = 1)
abstract class AlarmDatabase: RoomDatabase() {
    abstract fun getAlarmDao(): AlarmDao

    companion object {

        private var INSTANCE: AlarmDatabase? = null

        fun getInstance(context: Context): AlarmDatabase? {

            if(INSTANCE == null) {
                synchronized(AlarmDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context, AlarmDatabase::class.java, "alarm.db").build()
                }
            }

            return INSTANCE
        }

    }

}