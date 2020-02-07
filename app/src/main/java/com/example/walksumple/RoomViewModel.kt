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

    val stepList: MutableLiveData<ArrayList<StepEntity>> = MutableLiveData()

    private var mstepList: ArrayList<StepEntity>

    init {
        mstepList = repository.allstep
        stepList.postValue(mstepList)
    }

    fun insert(entity: StepEntity) = viewModelScope.launch { repository.insert(entity) }
    fun update(entity: StepEntity) = viewModelScope.launch{ repository.update(entity) }
    fun


//    fun serchStep(date: Long) = viewModelScope.launch { repository.getsum(date).let {list->
//        list.map { insert(StepEntity(date,it)) }
//        mstepList = list
//        stepList.postValue(mstepList)
//    } }
}