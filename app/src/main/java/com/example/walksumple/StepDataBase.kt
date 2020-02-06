package com.example.walksumple

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StepEntity::class],version = 1,exportSchema = false)
abstract class StepDataBase : RoomDatabase(){
    abstract fun dao(): StepDao

    companion object{

        @Volatile
        private  var instance: StepDataBase? = null

        fun getInstance(context: Context): StepDataBase = instance ?: synchronized(this){
            Room.databaseBuilder(context,StepDataBase::class.java,"step.db").build()
        }
    }
}