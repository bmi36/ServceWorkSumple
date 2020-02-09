package com.example.walksumple

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StepDao{
    @Query("select sum(step) from entity where data like :data ||'%' group by data ||'%'")
    fun serch(data: Long): IntArray

    @Insert
    suspend fun insert(entity: StepEntity)

    @Update
    suspend fun update(entity: StepEntity)

    @Query("select sum(step) from entity where data between :year || 01 || '%' and :year || 12 || '%' group by data")
    fun getMonth(year: Long): IntArray
}