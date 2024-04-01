package com.practicum.resp_toi_app.ui.parts

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.ui.theme.BottomNavColor
import com.practicum.resp_toi_app.ui.theme.backgroundCardColor
import com.practicum.resp_toi_app.ui.theme.cardNoActiveAlarmBackground
import com.practicum.resp_toi_app.ui.theme.cardNoActiveBackground
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.ui.viewModel.TestCallState


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ForegroundServiceType")
@Composable
fun RenderSettingsScreen(viewModel: MainViewModel) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var timerButtonEnabled by remember { mutableStateOf(true) }
    var sheetTimerButtonText by remember { mutableStateOf("Запустить") }
    val context = LocalContext.current as Activity

    val timer by viewModel.testCallTimer.collectAsState()

    val testCallState = viewModel.testCallState.collectAsState()

    when (testCallState.value) {
        is TestCallState.Loading -> {
            timerButtonEnabled = false
            sheetTimerButtonText = "Отправка запроса..."
        }
        is TestCallState.Error -> {
            Toast.makeText(LocalContext.current, "Ошибка при отправке запроса на сервер", Toast.LENGTH_SHORT).show()
            viewModel.errorMessageShown()
        }
        is TestCallState.InProcess -> {
            timerButtonEnabled = false
            sheetTimerButtonText = "Закройте приложение"
        }

        is TestCallState.EnabledAfterUse -> {
            sheetTimerButtonText = "Попробовать ещё раз"
            timerButtonEnabled = true
        }
        is TestCallState.EnabledFirstTime -> {
            timerButtonEnabled = true
            sheetTimerButtonText = "Запустить"
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(brush = gradientBackGroundBrush)
        .padding(horizontal = 6.dp, vertical = 12.dp)
    ) {
        ElevatedCard(
            colors = CardDefaults.cardColors(containerColor = backgroundCardColor),
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(vertical = 14.dp, horizontal = 14.dp)
            ) {
                Text(
                    text = "Это приложение полностью бесплатное",
                    color = Color.White,
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
                )
                Text(
                    modifier = Modifier.padding(top = 18.dp),
                    color = Color.White,
                    text = "Я начинающий Android-разработчик и ищу работу! Если у вас есть друзья и знакомые разработчики, я буду благодарен за рекомендацию."
                )
                Text(
                    modifier = Modifier.padding(top = 18.dp),
                    color = Color.White,
                    text = "Спасибо, что ставите оценки в Google Play и на мою страницу в GitHub, это сильно помогает мне в портфолио."
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
                    contentDescription = "do",
                    tint = Color.White
                )
            }
        }

        TextButton(
            onClick = {
                val openTelegram = Intent(Intent.ACTION_VIEW)
                val appName = "org.telegram.messenger"

                openTelegram.setData(Uri.parse("https://t.me/dima_ret"))

                if (isAppAvailable(context, appName)) {
                    openTelegram.setPackage(appName)
                }
                context.startActivity(openTelegram)
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
                    contentDescription = "do",
                    tint = Color.White
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
                            modifier = Modifier.padding(bottom = 88.dp),
                            enabled = timerButtonEnabled,
                            onClick = {
                                viewModel.setTestCall()
                            }
                        ) {
                            Text(sheetTimerButtonText)
                        }
                }
            }
        }
    }
}

private fun isAppAvailable(context: Context, appName: String?): Boolean {
    val pm = context.packageManager
    return try {
        pm.getPackageInfo(appName!!, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: NameNotFoundException) {
        false
    }
}

