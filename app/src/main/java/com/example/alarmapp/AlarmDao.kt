package com.example.alarmapp

import androidx.room.*
import io.reactivex.Flowable

@Dao
interface AlarmDao{
    @Query("SELECT * FROM alarm")
    fun getAllAlarm(): Flowable<List<Alarm>>

    @Query("DELETE FROM alarm")
    fun clearAll()

    @Query("Delete FROM alarm WHERE id = :alarmId")
    fun deletById(alarmId:Long)

    @Query("Update alarm SET mill = :time , id =:time WHERE id = :alarmId")
    fun updateById(time : Long, alarmId: Long)

    //이미 저장된 항목이 있을 경우 데이터를 덮어쓴다
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg alarm: Alarm)

    //헤당 데이터를 업데이트 합니다.
    @Update
    fun update(vararg alarm: Alarm)

    //해당 데이터를 삭제합니다.
    @Delete
    fun delete(vararg alarm: Alarm)
}