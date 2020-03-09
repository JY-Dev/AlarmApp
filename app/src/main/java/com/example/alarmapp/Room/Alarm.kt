package com.example.alarmapp.Room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Alarm(@PrimaryKey val id: Long, val mill : Long)