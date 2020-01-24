package com.example.walksumple

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.sql.SQLException

class SteoViewModel(application: Application) : AndroidViewModel(application){
    private val repository: StepRepository = StepDataBase.getInstance(application).dao().let {
        StepRepository(it)
    }

    fun getStep(id: Long) = viewModelScope.launch { getsum(id) }
    fun getMonth(year: Long) = viewModelScope.launch { getmonth(year) }

    fun insert(entity: StepEntity) = viewModelScope.launch { repository.insert(entity) }
    fun update(entity: StepEntity) = viewModelScope.launch{ repository.update(entity) }
    suspend fun getsum(id: Long): List<Int> = withContext(Dispatchers.Default) { repository.getsum(id) }

    suspend fun getmonth(year: Long): List<Int> = withContext(Dispatchers.Default){ repository.getMonth(year) }
    fun UandI(entity: StepEntity) = try { insert(entity) }catch (e: SQLException){ update(entity) }
}