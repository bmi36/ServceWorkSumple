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
import androidx.lifecycle.*


class StepService : Service(), SensorEventListener, LifecycleOwner {

    private lateinit var mSensorManager: SensorManager
    private lateinit var mstepConterSensor: Sensor
    private lateinit var serviceViewModel: StepViewModel
    var step = 0
    private val dispatcher = ServiceLifecycleDispatcher(this)
    var list: ArrayList<StepEntity>? = arrayListOf()
    //    private val manager = WorkManager.getInstance(this)
    override fun onCreate() {
        super.onCreate()
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mstepConterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        mSensorManager.unregisterListener(this, mstepConterSensor)

        serviceViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(StepViewModel::class.java)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return StepBindar()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            serviceViewModel.getStep(step++)
            Log.d("step", step.toString())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        serviceViewModel.stepEntity.observe(this, Observer {
            list?.let { list ->
                if (list.isEmpty()) list.add(it) else list.find { element -> element.id == it.id }.let { element ->
                    if (element != null) element.step = it.step
                }
                list.forEach { element -> if (element.id == it.id) element.step = it.step }
            } ?: arrayListOf(it)
        })
        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
        mSensorManager.registerListener(this, mstepConterSensor, SensorManager.SENSOR_DELAY_NORMAL)
        getSharedPreferences("STEP", Context.MODE_PRIVATE).edit().putInt("step", step).apply()
    }

    fun defListe(){ list = null }

    inner class StepBindar : Binder() {
        fun getBindar(): StepService = this@StepService
    }

    override fun getLifecycle(): Lifecycle = dispatcher.lifecycle
}