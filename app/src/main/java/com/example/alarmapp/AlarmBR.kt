package com.example.alarmapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager


class AlarmBR : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent : Intent?) {
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(WorkerManager::class.java).build()
        WorkManager.getInstance().enqueue(oneTimeWorkRequest)
    }



}