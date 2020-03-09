package com.example.alarmapp.Noti

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.alarmapp.Room.DataProcess


/*
 알람 BroadcastReceiver
 */
class AlarmBR : BroadcastReceiver(){
    private val dataProcess = DataProcess()
    override fun onReceive(context: Context?, intent : Intent?) {
        if (intent?.action.equals("android.intent.action.LOCKED_BOOT_COMPLETED") || intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            if (context!=null) {
                System.out.println("context is not null")
                if (getInt(context,"count") == 0) setInt(context,"count",1)
                when(getInt(context,"count")){
                    1 -> {
                        dataProcess.loadBackUpData(context)
                        setInt(context,"count",2)
                    }
                    2 -> {
                        setInt(context,"count",0)
                        sendData(intent)
                    }
                }
            } else System.out.println("context is null")

        } else {
            sendData(intent)
        }

    }
    fun setInt(context: Context, key: String?, value: Int) {
        val preferences = context.getSharedPreferences("CHECKCOUNT", Context.MODE_PRIVATE)
        val prefs: SharedPreferences = preferences
        val editor = prefs.edit()
        editor.putInt(key, value)
        editor.commit()
    }
    fun getInt(context: Context, key: String?): Int {
        val preferences = context.getSharedPreferences("CHECKCOUNT", Context.MODE_PRIVATE)
        val prefs: SharedPreferences = preferences
        return prefs.getInt(key, 0)
    }
    fun sendData(intent: Intent?){
        System.out.println("test2="+intent?.action)
        val idLong = if (intent?.getLongExtra("test",0L) != null)intent.getLongExtra("test",0L) else 0L
        val inputData = Data.Builder().putLong("id",idLong).build()
        //Workmanager 설정
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(WorkerManager::class.java).setInputData(inputData).build()
        WorkManager.getInstance().enqueue(oneTimeWorkRequest)

    }
}