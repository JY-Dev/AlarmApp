package com.example.alarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var hour = 0
    private var min = 0
    private var millis: Long = 0L
    private val timeFormatt = TimeFormatt()
    private val cal = Calendar.getInstance()
    private val cal2 = Calendar.getInstance()
    private lateinit var mAdpater : AlarmListAdpater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        millis = cal.timeInMillis
        loadData(this)
        mAdpater = AlarmListAdpater(this)

        alarm_list.adapter = mAdpater

        alarm_reg_btn.setOnClickListener {
            setTime()
            cal2.timeInMillis = timeFormatt.calAlarmTime(millis)
            Toast.makeText(this,timeFormatt.timeToString1(cal2),Toast.LENGTH_LONG).show()
            insertData(this,millis)

        }
    }

    private fun setTime(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            hour = time_picker.hour
            min = time_picker.minute
        } else {
            hour = time_picker.currentHour
            min = time_picker.currentMinute
        }
        cal.set(Calendar.HOUR_OF_DAY,hour)
        cal.set(Calendar.MINUTE,min)
        cal.set(Calendar.SECOND,0)
        millis = timeFormatt.detailSetTIme(cal.timeInMillis)
    }

    private fun startBroadcast() {
        val intent = Intent(this, AlarmBR::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(applicationContext, timeFormatt.checkPreTime(millis).toInt(), intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        when {
            Build.VERSION.SDK_INT >= 23 -> alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,timeFormatt.checkPreTime(millis),pendingIntent)
            Build.VERSION.SDK_INT >= 19 -> alarmManager.setExact(AlarmManager.RTC_WAKEUP,timeFormatt.checkPreTime(millis),pendingIntent)
            else -> alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,timeFormatt.checkPreTime(millis),AlarmManager.INTERVAL_DAY,pendingIntent)
        }

    }

    private fun insertData(context:Context , mil : Long){
        val alarm = Alarm(mil,mil)

        Observable.just(alarm)
            .subscribeOn(Schedulers.io())
            .subscribe( {
                AlarmDatabase.getInstance(context)
                    ?.getAlarmDao()
                    ?.insert(alarm)
                startBroadcast()

            },
                {
                })
    }

    private fun loadData(context: Context){
        AlarmDatabase
            .getInstance(context)!!
            .getAlarmDao()
            .getAllAlarm()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {}
                .doOnTerminate {}
                    .subscribe( {
                        mAdpater.setItem(it.toMutableList())
                    }, {

                        Log.e("MyTag", it.message)

                    })
    }


}
