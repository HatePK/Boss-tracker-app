package com.practicum.resp_toi_app.ui

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class NotificationActivity() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        showWhenLockedAndTurnScreenOn()
        super.onCreate(savedInstanceState)

        var title: String? = "RESP-TOI"
        var body: String? = "Оповещение о смерти РБ"

        if (intent.extras !== null) {
            title = intent.extras?.getString("title")
            body = intent.extras?.getString("body")
        }

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight(800),
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center
                        ),
                        text = "$title\n$body"
                    )
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "notification",
                        modifier = Modifier.padding(bottom = 80.dp).size(160.dp),
                        tint = Color.White
                    )
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        onClick = {
                            cancelNotification()
                            finish()
                        }
                    ) {
                        Text(text = "Скрыть уведомление", style = TextStyle(color = Color.White))
                    }
                }

            }
        }
    }

    private fun showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }

    private fun cancelNotification() {
        val ns = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        ns.cancelAll()
    }
}