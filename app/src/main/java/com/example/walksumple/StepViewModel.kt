package com.example.walksumple

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class StepViewModel : ViewModel() {


    private val stepEntity: MutableLiveData<StepEntity> = MutableLiveData()
    var mstepEntity: StepEntity? = null

    fun getStep(step: Int){
        val date = SimpleDateFormat("yyyyMMdd", Locale.JAPAN).format(Calendar.getInstance().time)
        mstepEntity = StepEntity(date.toLong(),step)
        stepEntity.postValue(mstepEntity)
    }

    //    private val stepArray: MutableLiveData<ArrayList<StepEntity>> = MutableLiveData()
//    var mstepArray: ArrayList<StepEntity>? = null


//    fun uploadList(step: Int){
//        val date = SimpleDateFormat("yyyyMMdd", Locale.UK).format(Calendar.getInstance().time)
//        mstepArray?.let {arraylist->
//            arraylist.forEach { if (it.id == date.toLong()) it.step = step }
//        stepArray = arraylist
//    }
}