package com.example.alarmapp

import android.app.Application

class MyApplication : Application() {
    var flagBool = true
    override fun onCreate() {
        super.onCreate()
        flagBool = true
    }

    fun setFlag(bl : Boolean){
        flagBool = bl
    }

    fun getFlag() : Boolean{
        return flagBool
    }
}