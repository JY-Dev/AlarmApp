package com.example.alarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*


class AlarmListAdpater(context:Context) : BaseAdapter(){

    private var mContext: Context = context
    var alarmList : MutableList<Alarm> = mutableListOf()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view : View
        val holder : ViewHolder
        if (convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.adapterlayout,null)
            holder = ViewHolder()
            holder.tv1 = view.findViewById(R.id.tv1)
            holder.deleteBtn = view.findViewById(R.id.delete_btn)
            holder.modifyBtn = view.findViewById(R.id.modify_btn)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = alarmList[position].mill
        holder.tv1?.text = TimeFormatt().timeToString2(calendar)

        holder.deleteBtn?.setOnClickListener {
            Observable.just(AlarmDatabase.getInstance(mContext))
                .subscribeOn(Schedulers.io())
                .subscribe {
                    it?.getAlarmDao()?.deletById(alarmList[position].id)
                    cancelAlarm(alarmList[position].id)
                }
        }

        holder.modifyBtn?.setOnClickListener {
            val modifyCal = Calendar.getInstance()
            modifyCal.timeInMillis = alarmList[position].mill
            TimePickerDialog(mContext, modifyAlarm(alarmList[position].id), modifyCal.get(Calendar.HOUR_OF_DAY), modifyCal.get(Calendar.MINUTE), false).show()
        }

        DataProcess().dataSave(mContext,alarmList)

        return view
    }


    fun modifyAlarm(id : Long) = TimePickerDialog.OnTimeSetListener{ timePicker: TimePicker, hour: Int, min: Int ->
        val cal  = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY,hour)
        cal.set(Calendar.MINUTE,min)
        cal.set(Calendar.SECOND,0)
        val mils = TimeFormatt().detailSetTIme(cal.timeInMillis)
        Observable.just(AlarmDatabase.getInstance(mContext))
            .subscribeOn(Schedulers.io())
            .subscribe {
                if (checkOverlap(mils)){
                    cancelAlarm(id)
                    it?.getAlarmDao()?.updateById(mils,id)
                    startBroadcast(mils)
                }
            }

        cal.timeInMillis = TimeFormatt().calAlarmTime(cal.timeInMillis)
        Toast.makeText(mContext,TimeFormatt().timeToString1(cal), Toast.LENGTH_LONG).show()
    }



    override fun getItem(position: Int): Any {
       return alarmList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return alarmList.size
    }

    fun setItem(itemList:MutableList<Alarm>){
        alarmList = itemList
        notifyDataSetChanged()
    }

    private fun startBroadcast(mil:Long) {
        val intent = Intent(mContext, AlarmBR::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(mContext.applicationContext, TimeFormatt().checkPreTime(mil).toInt(), intent, 0)
        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= 19) alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,TimeFormatt().checkPreTime(mil),AlarmManager.INTERVAL_DAY,pendingIntent)
        else alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,TimeFormatt().checkPreTime(mil),AlarmManager.INTERVAL_DAY,pendingIntent)
    }


    private fun checkOverlap(selId : Long) : Boolean{
        alarmList.forEach {
            if (it.id == selId) return false
        }
        return true
    }

    private fun cancelAlarm(id : Long){
        val alarmManger = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(mContext,AlarmBR::class.java)
        val pendingIntent = PendingIntent.getBroadcast(mContext,id.toInt(),intent,0)
        alarmManger.cancel(pendingIntent)
    }

    class ViewHolder{
        var tv1 : TextView? = null
        var modifyBtn : Button? = null
        var deleteBtn : Button? = null
    }

}