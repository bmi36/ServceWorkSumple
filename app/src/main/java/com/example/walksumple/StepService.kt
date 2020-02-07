package com.example.walksumple

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.time.Duration


class StepService : Service(), SensorEventListener{

    private lateinit var mSensorManager: SensorManager
    private lateinit var mstepConterSensor: Sensor
    private lateinit var serviceViewModel: StepViewModel
    var step = 0
    private val manager = WorkManager.getInstance(this)

    override fun onCreate() {
        super.onCreate()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mstepConterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        mSensorManager.unregisterListener(this, mstepConterSensor)


//        Toast.makeText(this,"はじめ",Toast.LENGTH_SHORT).show()
//        manager.enqueue(PeriodicWorkRequest.Builder(MyWork::class.java, Duration.ofMillis(15)).build())
    }

    override fun onBind(p0: Intent?): IBinder? {
        return StepBindar()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            serviceViewModel.getStep(step++)
            Log.d("step",step.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSensorManager.registerListener(this, mstepConterSensor, SensorManager.SENSOR_DELAY_NORMAL)
        getSharedPreferences("STEP", Context.MODE_PRIVATE).edit().putInt("step", step).apply()

    }
    inner class StepBindar : Binder() {
        fun getBindar(): StepService = this@StepService
    }
}