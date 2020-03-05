package com.example.alarmapp

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class WorkerManager(context: Context , workerParameters: WorkerParameters) : Worker(context,workerParameters) {
   private val notiChannelId = "test_alarm"
   private val notiChannelName = "test_alarm"
    private val mContext = context
    private val dataProcess = DataProcess()

    override fun doWork(): Result {
        val datas = dataProcess.dataLoad(mContext)
        if (datas !=null && datas.size > 0) {
            showNotification("알람","알람이 울렸습니다.",datas[0].id.toInt())
            val vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createOneShot(1000,50))
            datas.removeAt(0)
            dataProcess.dataSave(mContext,datas)
        }


        return Result.success()
    }

   private fun showNotification(title: String, task: String,id:Int) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notiChannelId,
                notiChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification =
            NotificationCompat.Builder(applicationContext, notiChannelId)
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.sym_def_app_icon)
        notificationManager.notify(id, notification.build())
    }

}