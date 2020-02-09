package com.example.walksumple

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StepRepository = StepDataBase.getInstance(application).dao().let {
        StepRepository(it)
    }

    val stepList: MutableLiveData<Int> = MutableLiveData()

    fun insert(entity: StepEntity) = viewModelScope.launch { repository.insert(entity) }
    fun update(entity: StepEntity) = viewModelScope.launch { repository.update(entity) }
    fun step(id: Long) =
        viewModelScope.launch(Dispatchers.IO) { stepList.postValue(repository.getsum(id).toTypedArray().sum()) }
}