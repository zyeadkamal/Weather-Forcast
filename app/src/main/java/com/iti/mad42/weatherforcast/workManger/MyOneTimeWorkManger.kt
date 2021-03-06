package com.iti.mad42.weatherforcast.workManger

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.iti.mad42.weatherforcast.MainActivity
import com.iti.mad42.weatherforcast.Utilities.getIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyOneTimeWorkManger (private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    private val CHANNEL_ID = 14
    private val channel_name = "CHANNEL_NAME"
    private val channel_description = "CHANNEL_DESCRIPTION"
    private var notificationManager: NotificationManager? = null
    var alertWindowManger: AlertWindowManger? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {

        val description = inputData.getString("description")!!
        val icon = inputData.getString("icon")!!
        val type = inputData.getString("type")
        Log.e("MyOneTimeWorkManger","doWork")

        when(type) {
            "alarm" -> {
                if (Settings.canDrawOverlays(context)) {
                    GlobalScope.launch(Dispatchers.Main) {
                        alertWindowManger = AlertWindowManger(context, description, icon)
                        alertWindowManger!!.setMyWindowManger()
                    }
                }
            }
            "notification" -> {
                notificationChannel()
                makeNotification(description, icon)
            }
        }
        return Result.success()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeNotification(description: String, icon: String){
        Log.e("MyOneTimeWorkManger","makeNotification")
        lateinit var builder: Notification.Builder

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val bitmap = BitmapFactory.decodeResource(context.resources, getIcon(icon))

        builder = Notification.Builder(applicationContext, "$CHANNEL_ID")
            .setSmallIcon(getIcon(icon))
            .setContentText(description)
            .setContentTitle("Weather Alarm")
            .setLargeIcon(bitmap)
            .setPriority(Notification.PRIORITY_DEFAULT)
            .setStyle(
                Notification.BigTextStyle()
                    .bigText(description)
            )
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setLights(Color.RED, 3000, 3000)
            .setAutoCancel(true)
        notificationManager?.notify(1234, builder.build())

    }

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("$CHANNEL_ID", channel_name, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.description = channel_description
            notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
            Log.e("MyOneTimeWorkManger","notificationChannel")

        }
    }
}