package com.example.alarmapp

import android.R
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class WorkerManager(context: Context , workerParameters: WorkerParameters) : Worker(context,workerParameters) {

    private val mContext = context
    private val dataProcess = DataProcess()

    override fun doWork(): Result {
            dataProcess.showNotification(mContext,"알람","알람이 울렸습니다.",inputData.getLong("id",0L).toInt())
            vibrator()
            dataProcess.deleteData(mContext,inputData.getLong("id",0L)) {}
        return Result.success()
    }

    private fun vibrator(){
        val vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(1000,50))
    }
}