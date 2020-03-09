package com.example.alarmapp.Activity

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapp.Adapter.AlarmListAdpater
import com.example.alarmapp.Room.DataProcess
import com.example.alarmapp.Formatt.TimeFormatt
import com.example.alarmapp.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var hour = 0
    private var min = 0
    private var millis: Long = 0L
    private val timeFormatt = TimeFormatt()
    private val cal = Calendar.getInstance()
    private val cal2 = Calendar.getInstance()
    private lateinit var mAdpater: AlarmListAdpater
    private val dataProcess = DataProcess()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        millis = cal.timeInMillis

        mAdpater = AlarmListAdpater(this)
        dataProcess.loadData(this, mAdpater)
        alarm_list.adapter = mAdpater

        alarm_reg_btn.setOnClickListener {
            setTime()
            cal2.timeInMillis = timeFormatt.calAlarmTime(millis)
            Toast.makeText(this, timeFormatt.timeToString1(cal2), Toast.LENGTH_LONG).show()
            dataProcess.insertData(this, millis)
            System.out.println("test=" + millis)

        }
    }

    /*
    시간 설정 함수
     */
    private fun setTime() {

        //시간 셋팅

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = time_picker.hour
            min = time_picker.minute
        } else {
            hour = time_picker.currentHour
            min = time_picker.currentMinute
        }

        //캘린더에 시간입력
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, min)
        cal.set(Calendar.SECOND, 0)

        //시간 밀리세컨드
        millis = timeFormatt.detailSetTIme(cal.timeInMillis)
    }

}
