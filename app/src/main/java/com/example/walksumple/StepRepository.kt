package com.example.walksumple

import androidx.annotation.WorkerThread

class StepRepository (private val dao: StepDao){

    @WorkerThread
    suspend fun insert(entity: StepEntity) = dao.insert(entity)
    suspend fun update(entity: StepEntity) = dao.update(entity)
    suspend fun getsum(date: Long): List<Int> = dao.getsumSteps(date)
    suspend fun getMonth(year: Long): List<Int> = dao.getMonth(year)
}