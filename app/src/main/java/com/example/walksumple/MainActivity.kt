package com.example.walksumple

import android.annotation.SuppressLint
import android.content.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var stepCounter: Int = 0
    private var sensorCounter: Int = 0
    private lateinit var prefs: SharedPreferences
    private lateinit var stepViewModel: SteoViewModel
    private var upFlg = false
    private var bound = false
    lateinit var intentService: Intent
    lateinit var service: StepService

    private val connection = object : ServiceConnection{
        override fun onServiceDisconnected(p0: ComponentName?) {
            bound = false
        }

        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            if(iBinder is StepService.StepBindar) {
                bound = true
                service = iBinder.getBindar()
//                service.viewModel.mBinder.observe(this@MainActivity, androidx.lifecycle.Observer { })
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        upFlg = false
        stepViewModel = ViewModelProvider(this)[SteoViewModel::class.java]
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE)
        intentService = Intent(this,StepService::class.java)
        bindService(intentService,connection,Context.BIND_AUTO_CREATE)
        stepCounter = prefs.getInt("walk", 0)
        sensorCounter = prefs.getInt("sensor", 0)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {

        when (event.sensor.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                if (upFlg) {
                    stepCounter = event.values[0].toInt() - sensorCounter
                }
                stepCounter++
            }
        }

        Log.d("counter", stepCounter.toString())

        stepsValue.text = (stepCounter - 1).toString()
    }

    override fun onStop() {
        super.onStop()
        if (bound) unbindService(connection).let { bound = false }

//        if (!dayFlg.isDoneDaily()) {
//            prefs.run {
//                val day = Date(System.currentTimeMillis()).toTypeDate()
//                val step = stepCounter
//                viewModel.UandI(StepEntity(day, step))
//                stepCounter = 0
//                edit().clear()
//                    .putInt("sensor",sensorCounter)
//                    .apply()
//            }
//        }else
//            prefs.edit().let {
//                it.putInt("sensor", sensorCounter)
//                it.putInt("walk", stepCounter)
//                it.apply()
//            }
        }

    }

    fun Long.toTypeDate() =
        SimpleDateFormat("yyyyMMdd", Locale.UK).format(this.toInt() / 1000000).toLong()

    fun Date.toTypeDate() =
        SimpleDateFormat("yyyyMMdd", Locale.UK).format(this).toLong()