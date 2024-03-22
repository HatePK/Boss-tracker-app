package com.practicum.resp_toi_app.ui.parts

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.os.CountDownTimer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.ui.theme.backgroundCardColor
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.theme.transparent
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ForegroundServiceType")
@Composable
fun RenderSettingsScreen() {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var timerButtonEnabled by remember { mutableStateOf(true) }
    var sheetTimerButtonText by remember { mutableStateOf("Запустить") }
    var timer by remember { mutableStateOf("30")}

    Column(modifier = Modifier
        .fillMaxSize()
        .background(brush = gradientBackGroundBrush)
        .padding(horizontal = 6.dp, vertical = 12.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .background(brush = gradientBackGroundBrush)
                    .padding(12.dp)
            ) {
                Text(
                    text = "Это приложение полностью бесплатное",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    color = Color.White,
                    text = "Я начинающий Android-разработчик (безработный, конечно же). Спасибо, что ставите оценки в Google Play, это сильно поможет мне в портфолио."
                )
            }
        }

        TextButton(
            onClick = {
                showBottomSheet = true
            },
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Box(modifier = Modifier.width(30.dp)) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification check",
                            modifier = Modifier.padding(end = 6.dp),
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Протестировать будильник",
                        color = Color.White,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "do"
                )
            }
        }

        TextButton(
            onClick = {
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row {
                    Box(modifier = Modifier.width(30.dp)) {
                        Icon(
                            painterResource(id = R.drawable.ic_telegram),
                            contentDescription = "send message",
                            modifier = Modifier.padding(start = 2.dp, end = 6.dp),
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "Написать в Telegram",
                        color = Color.White,
                        )
                }
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "do"
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Text("Через 30 секунд после запуска, вам на телефон поступит тестовый будильник. Чтобы убедиться, что всё работает правильно, полностью закройте приложение и заблокируйте экран.")
                        Text(
                            modifier = Modifier.padding(vertical = 20.dp),
                            style = TextStyle(fontSize = 74.sp),
                            text = "00:$timer"
                        )
                        Button(
                            modifier = Modifier.padding(bottom = 60.dp),
                            enabled = timerButtonEnabled,
                            onClick = {
                                timerButtonEnabled = false
                                sheetTimerButtonText = "Закройте приложение"
                                object: CountDownTimer(30000, 1000) {
                                    override fun onTick(msLost: Long) {
                                        if (msLost > 10000) {
                                            timer = (msLost / 1000).toString()
                                        } else {
                                            timer = "0${msLost / 1000}"
                                        }
                                    }
                                    override fun onFinish() {
                                        timer = "30"
                                        sheetTimerButtonText = "Попробовать ещё раз"
                                        timerButtonEnabled = true
                                    }
                                }.start()
                            }
                        ) {
                            Text(sheetTimerButtonText)
                        }
                }
            }
        }
    }
}

