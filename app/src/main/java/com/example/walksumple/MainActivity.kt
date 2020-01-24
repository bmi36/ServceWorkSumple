package com.example.walksumple

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var mSensorManager: SensorManager
    private lateinit var mStepDetectorSensor: Sensor
    private lateinit var mStepCanterSensor: Sensor

    private var stepCounter: Int = 0
    private var sensorCounter: Int = 0
    private lateinit var prefs: SharedPreferences
    private lateinit var viewModel: SteoViewModel
    private var upFlg = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        upFlg = false
        viewModel = ViewModelProviders.of(this)[SteoViewModel::class.java]
        dayFlg.execute()
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE)
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        mStepCanterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        stepCounter = prefs.getInt("walk", 0)
        sensorCounter = prefs.getInt("sensor", 0)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private val dayFlg = DayilyEventController(0, 0)

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

    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this, mStepCanterSensor, SensorManager.SENSOR_DELAY_NORMAL)

        mSensorManager.registerListener(
            this,
            mStepDetectorSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onStop() {
        super.onStop()
        mSensorManager.unregisterListener(this, mStepCanterSensor)
        mSensorManager.unregisterListener(this, mStepDetectorSensor)
        if (!dayFlg.isDoneDaily()) {
            prefs.run {
                val day = Date(System.currentTimeMillis()).toTypeDate()
                val step = stepCounter
                viewModel.UandI(StepEntity(day, step))
                stepCounter = 0
                edit().clear()
                    .putInt("sensor",sensorCounter)
                    .apply()

            }
        }else
            prefs.edit().let {
                it.putInt("sensor", sensorCounter)
                it.putInt("walk", stepCounter)
                it.apply()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun Long.toTypeDate() =
        SimpleDateFormat("yyyyMMdd").let { it.format(this.toInt() / 1000000).toLong() }

    @SuppressLint("SimpleDateFormat")
    fun Date.toTypeDate() =
        SimpleDateFormat("yyyyMMdd").let { it.format(this).toLong() }