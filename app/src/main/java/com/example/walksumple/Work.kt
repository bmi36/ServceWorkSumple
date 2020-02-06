package com.example.walksumple

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class MyWork(context: Context, params: WorkerParameters) : Worker(context, params) {

    val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val name = "Thuti"
    val id = "casareal_chanel"
    val notfy = "この通知の詳細情報をせっていします。"
    val simpleDateFormat = SimpleDateFormat("yyy-MM-dd HH:mm:dd", Locale.UK)

    companion object {
        var nid = 1
    }

    init {
        notificationManager.getNotificationChannel(id)
        val mChanel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH).apply {
            description = notfy
        }
        notificationManager.createNotificationChannel(mChanel)
    }

    override fun doWork(): Result {

        val notification =
            Notification.Builder(applicationContext, id).apply {
                setContentText("${nid}回目のメッセージ->${simpleDateFormat.format(Calendar.getInstance().time)}")
                setSmallIcon(R.drawable.ic_launcher_background)
            }
        notificationManager.notify(nid, notification.build())
        nid++
        return Result.success()
    }
}