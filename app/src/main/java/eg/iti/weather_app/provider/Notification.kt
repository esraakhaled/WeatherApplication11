package eg.iti.weather_app.provider

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import eg.iti.weather_app.R

class Notification (base: Context?, intent: Intent) : ContextWrapper(base) {
    private var notificationManager: NotificationManager? = null
    private lateinit var intent: Intent

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.intent = intent
            createChannel()
            createChannel2()
        }
    }

    companion object {
        const val channelID = "channelID"
        const val channelName = "Channel Name"
        const val channelID2 = "channelID2"
        const val channelName2 = "Channel Name2"

    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel() {

        val channel = NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.enableLights(true)
        channel.setLightColor(Color.RED)
        channel.enableVibration(true)
        manager!!.createNotificationChannel(channel)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel2() {

        val channel = NotificationChannel(channelID2, channelName2, NotificationManager.IMPORTANCE_HIGH)
        channel.enableLights(true)
        channel.setLightColor(Color.RED)
        channel.enableVibration(true)
        manager!!.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (notificationManager == null) {
                notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
        }

    val channelNotification: NotificationCompat.Builder
        get() = NotificationCompat.Builder(applicationContext, channelID)
            .setContentTitle("Alarm")
            .setContentText(intent.getStringExtra("main"))
            .setSmallIcon(R.drawable.ic_notification)

    val channelNotificationAlert: NotificationCompat.Builder
        get() = NotificationCompat.Builder(applicationContext, channelID2)
            .setContentTitle("Alert")
            .setContentText(intent.getStringExtra("event")+ " " + intent.getStringExtra("desc"))
            .setSmallIcon(R.drawable.ic_notification)

}