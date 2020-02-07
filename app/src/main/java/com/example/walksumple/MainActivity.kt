package com.example.walksumple

import android.content.*
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var stepCounter: Int = 0
    private var sensorCounter: Int = 0
    private lateinit var prefs: SharedPreferences
    private lateinit var roomModel: RoomViewModel
    private var upFlg = false
    private var bound = false
    lateinit var intentService: Intent
    lateinit var service: StepService

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            bound = false
        }

        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            if (iBinder is StepService.StepBindar) {
                bound = true
                service = iBinder.getBindar()
                roomModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                    .create(RoomViewModel::class.java).apply {
                    service.list?.map { insert(it) }
                    service.defListe()
                }
                roomModel.stepList.observe(this@MainActivity, androidx.lifecycle.Observer {
                    stepsValue.text = it.toString()
                })
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        upFlg = false
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE)
        intentService = Intent(this, StepService::class.java)
        bindService(intentService, connection, Context.BIND_AUTO_CREATE)
        stepCounter = prefs.getInt("walk", 0)
        sensorCounter = prefs.getInt("sensor", 0)
    }

    override fun onStop() {
        super.onStop()
        if (bound) unbindService(connection).let { bound = false }
    }

}

fun Long.toTypeDate() =
    SimpleDateFormat("yyyyMMdd", Locale.UK).format(this.toInt() / 1000000).toLong()

fun Date.toTypeDate() =
    SimpleDateFormat("yyyyMMdd", Locale.UK).format(this).toLong()