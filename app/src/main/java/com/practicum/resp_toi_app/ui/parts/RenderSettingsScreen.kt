package com.practicum.resp_toi_app.ui.parts

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat.startForeground
import androidx.core.content.ContextCompat.getSystemService
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.ui.MainActivity
import com.practicum.resp_toi_app.ui.NotificationActivity

@SuppressLint("ForegroundServiceType")
@Composable
fun RenderSettingsScreen() {
    val context = LocalContext.current

    Button(onClick = {
        val fullScreenIntent = Intent(context, NotificationActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
            fullScreenIntent, PendingIntent.FLAG_IMMUTABLE)

        var builder = NotificationCompat.Builder(context, "Default")
            .setSmallIcon(R.drawable.ic_home)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(fullScreenPendingIntent, true)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            val uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
            builder.setSound(uri);
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(2, builder.build())
        }

    }) {
        Text(text = "Show notification")
    }
}