package com.example.alarmapp

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimeFormatt{
    private val oneDayMill = 86400000
    fun timeToString1(cal : Calendar) : String{
        return if (calHour(cal)>0&&calMin(cal)>0) String.format("%02d시 %02d분 뒤에 알람이 울립니다.", calHour(cal),calMin(cal))
        else if (calMin(cal)>0) String.format("%02d분뒤에 알람이 울립니다.",calMin(cal))
        else String.format("%02d초뒤에 알람이 울립니다.",calSec(cal))
    }

    fun timeToString2(selCal : Calendar) : String{
        val date = selCal.timeInMillis
        val formatter: DateFormat = SimpleDateFormat("HH시 mm분 알람", Locale.getDefault())
        return formatter.format(date)
    }

    fun calAlarmTime(selectMil : Long) : Long{
        val calAlarmsTime = selectMil - System.currentTimeMillis()
        return if (calAlarmsTime > 0) calAlarmsTime
        else oneDayMill+calAlarmsTime
    }

    fun checkPreTime(mil : Long) : Long {
        val calAlarmsTime = mil - System.currentTimeMillis()
        return if (calAlarmsTime > 0) mil
        else oneDayMill+mil+calAlarmsTime
    }

    fun calHour(cal: Calendar) : Long{
        return TimeUnit.MILLISECONDS.toHours(cal.timeInMillis)
    }

    fun calMin(cal: Calendar) : Long{
        return TimeUnit.MILLISECONDS.toMinutes(cal.timeInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(cal.timeInMillis))
    }

    private fun calSec(cal: Calendar) : Long{
        return TimeUnit.MILLISECONDS.toSeconds(cal.timeInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(cal.timeInMillis))
    }

    fun detailSetTIme(mil : Long) : Long{
        val buffer = StringBuffer(mil.toString())
        val length = buffer.length-1
        var count = 0
        for (i in length downTo 1){
            count++
            if (count>3) break
            buffer.setCharAt(i,'0')
        }
        return buffer.toString().toLong()

    }


}