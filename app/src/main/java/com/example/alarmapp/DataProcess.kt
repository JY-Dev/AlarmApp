package com.example.alarmapp

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class DataProcess{
    fun dataLoad(context: Context) : MutableList<Alarm>?{
        val listType : TypeToken<MutableList<Alarm>> = object : TypeToken<MutableList<Alarm>>() {}
        val gson = GsonBuilder().create()
        val sp = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val strContact = sp.getString("oneMessage", "")
// 변환
        val datas : MutableList<Alarm>? = gson.fromJson(strContact,listType.type)
        return datas
    }
    fun dataSave(context: Context, data : MutableList<Alarm>) {
        val listType : TypeToken<MutableList<Alarm>> = object : TypeToken<MutableList<Alarm>>() {}
        val gson = GsonBuilder().create()
        val userLocalData = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = userLocalData!!.edit()
        editor.clear()
        editor.commit()

// 데이터를 Json 형태로 변환

        val str = gson.toJson(data,listType.type)
        editor.putString("oneMessage", str) // Json 으로 변환한 객체 저장
        editor.commit()
    }
}