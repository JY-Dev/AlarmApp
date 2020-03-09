package com.example.alarmapp.Noti

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.alarmapp.Room.DataProcess


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