package com.example.alarmapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Alarm(@PrimaryKey val id: Long, val mill : Long)