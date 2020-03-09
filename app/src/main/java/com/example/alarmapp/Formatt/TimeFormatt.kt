package com.example.alarmapp.Formatt

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TimeFormatt{
    private val oneDayMill = 86400000

    //Toast SetString
    fun timeToString1(cal : Calendar) : String{
        return if (calHour(cal)>0&&calMin(cal)>0) String.format("%02d시 %02d분 %02d초 뒤에 알람이 울립니다.", calHour(cal),calMin(cal),calSec(cal))
        else if (calMin(cal)>0) String.format("%02d분 %02d초 뒤에 알람이 울립니다.",calMin(cal),calSec(cal))
        else String.format("%02d초뒤에 알람이 울립니다.",calSec(cal))
    }

    // listSetText
    fun timeToString2(selCal : Calendar) : String{
        val date = selCal.timeInMillis
        val formatter: DateFormat = SimpleDateFormat("HH시 mm분 알람", Locale.getDefault())
        return formatter.format(date)
    }


    fun calAlarmTime(selectMil : Long) : Long{
        // 현재시간차
        val calAlarmsTime = selectMil - System.currentTimeMillis()
        // 현재시간보다 클경우 현재시간차 RETURN
        return if (calAlarmsTime > 0) calAlarmsTime
        // 현재시간보다 작을경우 다음날 시간 RETURN
        else oneDayMill+calAlarmsTime
    }

    //지난 시간인지 CHECK
    fun checkPreTime(mil : Long) : Long {
        val calAlarmsTime = mil - System.currentTimeMillis()
        return if (calAlarmsTime > 0) mil
        else oneDayMill+mil+calAlarmsTime
    }

    //시 계산
    fun calHour(cal: Calendar) : Long{
        return TimeUnit.MILLISECONDS.toHours(cal.timeInMillis)
    }

    //분 계산
    fun calMin(cal: Calendar) : Long{
        return TimeUnit.MILLISECONDS.toMinutes(cal.timeInMillis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(cal.timeInMillis))
    }

    //초 계산
    private fun calSec(cal: Calendar) : Long{
        return TimeUnit.MILLISECONDS.toSeconds(cal.timeInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(cal.timeInMillis))
    }

    //시간 디테일하게 set timemillisecond 뒤에 3자리 000으로 초기화
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