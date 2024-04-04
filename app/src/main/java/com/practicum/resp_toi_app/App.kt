package com.practicum.resp_toi_app

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import com.practicum.resp_toi_app.utils.SharedPreferencesManager
import com.practicum.resp_toi_app.utils.refreshFcmToken
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)

        refreshFcmToken()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val mChannel = NotificationChannel("Default", "Будильник", NotificationManager.IMPORTANCE_HIGH)
            mChannel.description = "Канал для уведомлений"
            mChannel.enableVibration(true)
            mChannel.enableLights(true)
            mChannel.setSound(uri, audioAttributes)
            mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            notificationManager.createNotificationChannel(mChannel)
        }
    }
}