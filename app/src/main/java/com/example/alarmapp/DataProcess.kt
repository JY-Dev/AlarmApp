package com.example.alarmapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class DataProcess {
    private val timeFormatt = TimeFormatt()
    private val notiChannelId = "test_alarm"
    private val notiChannelName = "test_alarm"
    //Room data insert
    fun insertData(context: Context, mil: Long) {
        val alarm = Alarm(mil, mil)

        Observable.just(alarm)
            .subscribeOn(Schedulers.io())
            .subscribe({
                AlarmDatabase.getInstance(context)
                    ?.getAlarmDao()
                    ?.insert(alarm)
                //브로드캐스트 시작
                startBroadcast(context, mil)

            },
                {
                })
    }

    //Room data load
    fun loadData(context: Context, mAdpater: AlarmListAdpater) {
        AlarmDatabase
            .getInstance(context)!!
            .getAlarmDao()
            .getAllAlarm()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {}
            .doOnTerminate {}
            .subscribe({
                //adapter dataset
                mAdpater.setItem(it.toMutableList())
            }, {

                Log.e("MyTag", it.message)

            })
    }

    fun deleteData(mContext: Context, dataid: Long, callBack: () -> Unit) {
        Observable.just(AlarmDatabase.getInstance(mContext))
            .subscribeOn(Schedulers.io())
            .subscribe {
                it?.getAlarmDao()?.deletById(dataid)
                callBack()
            }
    }

    //Room data load
    fun loadBackUpData(context: Context) {
        AlarmDatabase
            .getInstance(context)!!
            .getAlarmDao()
            .getAllAlarm()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {}
            .doOnTerminate {}
            .subscribe({
                it.forEach {
                    println("testdata=" + it.mill)
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = it.id
                    if (it.mill < System.currentTimeMillis()) {
                        System.out.println("nononono_" + cal.get(Calendar.HOUR_OF_DAY) + "시_" + cal.get(Calendar.MINUTE) + "분_"
                        )
                        showNotification(context, "알람", "\"전원이 꺼진 동안 " + cal.get(Calendar.HOUR_OF_DAY) + "시 " + cal.get(Calendar.MINUTE) + "분에 " + "알람이 울렸습니다.",
                            it.mill.toInt()
                        )
                        deleteData(context, it.mill) {}
                        println("test2=" + it.mill)
                    } else {
                        startBroadcast(context, it.mill)
                    }
                }
            }, {

                Log.e("MyTag", it.message)

            })
    }

    fun startBroadcast(context: Context, millis: Long) {
        //브로드캐스트 intent 설정
        val intent = Intent(context, AlarmBR::class.java)
        intent.putExtra("test", millis)
        //pendingintent 설정
        val pendingIntent =
            PendingIntent.getBroadcast(
                context.applicationContext,
                timeFormatt.checkPreTime(millis).toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //알람 매니져 set 특정시간에 발동되도록
        when {
            Build.VERSION.SDK_INT >= 23 -> alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeFormatt.checkPreTime(millis),
                pendingIntent
            )
            Build.VERSION.SDK_INT >= 19 -> alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                timeFormatt.checkPreTime(millis),
                pendingIntent
            )
            else -> alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP, timeFormatt.checkPreTime(millis),
                AlarmManager.INTERVAL_DAY, pendingIntent
            )
        }

    }

    fun showNotification(context: Context, title: String, task: String, id: Int) {

        val notificationManager =
            context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                notiChannelId,
                notiChannelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification =
            NotificationCompat.Builder(context.applicationContext, notiChannelId)
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(id, notification.build())
    }
}