package com.example.walksumple

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.sql.SQLException

class RoomViewModel(application: Application) : AndroidViewModel(application){
    private val repository: StepRepository = StepDataBase.getInstance(application).dao().let {
        StepRepository(it)
    }

    val stepList: MutableLiveData<Int> = MutableLiveData()

    var mstepList: Int? = null

    fun insert(entity: StepEntity) = viewModelScope.launch { repository.insert(entity) }
    fun update(entity: StepEntity) = viewModelScope.launch{ repository.update(entity) }
    fun serchStep(date: Long) = repository.getsum(date).let {list->
        list.map { insert(StepEntity(date,it)) }
        mstepList = list.sum()
        stepList.postValue(mstepList)
    }
}