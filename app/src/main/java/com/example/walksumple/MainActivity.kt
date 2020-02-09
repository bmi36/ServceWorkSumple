package com.example.walksumple

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.forEach as forEach1

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var sensorCounter: Int = 0
    private lateinit var prefs: SharedPreferences
    private lateinit var roomModel: RoomViewModel
    private var upFlg = false
    private var bound = false
    private lateinit var intentService: Intent
    lateinit var service: StepService

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            bound = false
        }

        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder) {
            if (iBinder is StepService.StepBindar) {
                bound = true
                service = iBinder.getBindar()
                val date =
                    SimpleDateFormat("yyyyMMdd", Locale.JAPAN).format(Calendar.getInstance().time)
                val stepPrefs = getSharedPreferences("STEP", Context.MODE_PRIVATE)
                val flgPrefs = getSharedPreferences("FLAG",Context.MODE_PRIVATE).all.map { Pair<String,Boolean>(it.key,it.value as Boolean) }
                var flg = false
                flgPrefs.forEach1 { pair->
                     flg = if (date == pair.first) pair.second else false
                }

                stepPrefs.all.apply {
                    map {
                        val entity = StepEntity(it.key.toLong(), it.value.toString().toInt())

                        if (flg) { roomModel.insert(entity) }else{ roomModel.update(entity) }
                    }
                }
                val step = stepPrefs.getInt(date, 0)
                stepsValue.text = step.toString()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startService(intentService)
        bindService(intentService, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        upFlg = false
        prefs = getSharedPreferences("user", Context.MODE_PRIVATE)
        intentService = Intent(this, StepService::class.java)
        sensorCounter = prefs.getInt("sensor", 0)
        roomModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(RoomViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        Log.d("bind", bound.toString())
    }


    override fun onPause() {
        super.onPause()
        Log.d("bind", bound.toString())
    }

    override fun onStop() {
        super.onStop()
        stopService(intentService)
        unbindService(connection)
    }

}
fun Long.toTypeDate() =
    SimpleDateFormat("yyyyMMdd", Locale.JAPAN).format(this.toInt() / 1000000).toLong()

fun Date.toTypeDate() =
    SimpleDateFormat("yyyyMMdd", Locale.JAPAN).format(this).toLong()