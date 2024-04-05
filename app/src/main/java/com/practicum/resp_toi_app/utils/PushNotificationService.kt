package com.practicum.resp_toi_app.utils

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.ui.NotificationActivity

class PushNotificationService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.d("ABOBA", token)
        SharedPreferencesManager.saveString("Token", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.data.let {
            val oneTimeID = SystemClock.uptimeMillis().toInt()

            val fullScreenIntent = Intent(this, NotificationActivity::class.java).apply {
                putExtra("title", it["title"])
                putExtra("body", it["body"])
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }

            val fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_MUTABLE)

            var builder = NotificationCompat.Builder(this, "Default")
                .setSmallIcon(R.drawable.ic_home)
                .setContentTitle(it["title"])
                .setContentText(it["body"])
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                val uri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM)
                builder.setSound(uri);
            }

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            NotificationManagerCompat.from(this).notify(oneTimeID, builder.build())
        }
    }
}