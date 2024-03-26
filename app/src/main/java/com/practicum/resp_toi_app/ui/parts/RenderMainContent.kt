package com.practicum.resp_toi_app.ui.parts

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.common.reflect.Reflection.getPackageName
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.ui.theme.TextNoActive
import com.practicum.resp_toi_app.ui.theme.backgroundCardColor
import com.practicum.resp_toi_app.ui.theme.cardActiveBackground
import com.practicum.resp_toi_app.ui.theme.cardNoActiveBackground
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.theme.progressBarBackground
import com.practicum.resp_toi_app.ui.theme.progressBarFillColor
import com.practicum.resp_toi_app.ui.viewModel.AlarmsState
import com.practicum.resp_toi_app.ui.viewModel.MainState
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.ui.viewModel.OneAlarmState
import com.practicum.resp_toi_app.utils.SharedPreferencesManager
import com.practicum.resp_toi_app.utils.getActivity
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderMainContent(
    data: List<BossEntity>,
    viewModel: MainViewModel,
    snackBar: SnackbarHostState
) {

    val refreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing,
        { viewModel.refresh() }
    )

    val alarmsState: AlarmsState by viewModel.alarmsState.collectAsState()

    Box() {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBackGroundBrush)
            .padding(horizontal = 6.dp)
            .pullRefresh(pullRefreshState)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(5.dp, vertical = 10.dp),
                    text = "Страница обновляется автоматически раз в минуту. Чтобы обновить принудительно, потяните вверх.",
                    style = TextStyle(fontSize = 14.sp),
                    color = TextNoActive
                )
            }
            itemsIndexed(data) {index, item ->
                val cardPadding = if (index == data.lastIndex) {
                    PaddingValues(bottom = 16.dp)
                } else {
                    PaddingValues(bottom = 8.dp)
                }

                if (item.timeFromDeath > 1080) {
                    val progressPercentage by remember {
                        mutableFloatStateOf(countPercentage(item.timeFromDeath))
                    }
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = backgroundCardColor,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(cardPadding),
                        elevation = CardDefaults.cardElevation()
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(
                                        shape = RoundedCornerShape(
                                            bottomStart = 12.dp,
                                            bottomEnd = 12.dp,
                                        )
                                    )
                                    .background(brush = cardActiveBackground)
                                    .padding(
                                        start = 20.dp,
                                        end = 12.dp,
                                        top = 26.dp,
                                        bottom = 26.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(0.3f),
                                ) {
                                    Text(
                                        style = TextStyle(fontSize = 30.sp),
                                        color = White,
                                        text = item.name
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 18.dp),
                                        style = TextStyle(fontSize = 16.sp),
                                        color = White,
                                        text = "${item.respStart} - ${item.respEnd} МСК"
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 18.dp),
                                        style = TextStyle(fontSize = 18.sp),
                                        color = White,
                                        text = "До конца ${countTimeBeforeResp(item.timeFromDeath)}"
                                    )
                                }
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        progress = progressPercentage,
                                        modifier = Modifier
                                            .padding(start = 15.dp, end = 5.dp)
                                            .size(140.dp),
                                        strokeWidth = 10.dp,
                                        color = progressBarFillColor,
                                        trackColor = progressBarBackground
                                    )
                                    Column(
                                        modifier = Modifier.padding(start = 15.dp, end = 5.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "Респ идёт уже",
                                            style = TextStyle(fontSize = 12.sp),
                                            color = White
                                        )
                                        Text(
                                            modifier = Modifier.padding(top = 3.dp),
                                            text = convertToTime(item.timeFromDeath),
                                            style = TextStyle(
                                                fontSize = 28.sp,
                                                fontWeight = FontWeight.W600
                                            ),
                                            color = White
                                        )
                                    }
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    painter = painterResource(id = R.drawable.ic_alarm),
                                    contentDescription = "alarm icon",
                                    tint = White
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                ) {
                                    when (alarmsState) {
                                        is AlarmsState.Loading -> renderProgressBar()
                                        is AlarmsState.Error -> renderAlarmError()
                                        is AlarmsState.Content -> {
                                            (alarmsState as AlarmsState.Content)
                                                .alarms[item.name]?.let {
                                                    renderAlarm(
                                                        alarmState = it,
                                                        boss = item.name,
                                                        viewModel = viewModel,
                                                        snackBar = snackBar
                                                    )
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Column {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = cardNoActiveBackground,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(cardPadding)
                        ){
                            Column(modifier = Modifier
                                .clip(
                                    shape = RoundedCornerShape(
                                        bottomStart = 12.dp,
                                        bottomEnd = 12.dp,
                                    )
                                )
                                .background(cardNoActiveBackground)
                                .padding(
                                    top = 30.dp,
                                    bottom = 35.dp
                                )
                                .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    style = TextStyle(fontSize = 30.sp),
                                    color = TextNoActive,
                                    text = item.name
                                )
                                Text(
                                    modifier = Modifier.padding(top = 10.dp),
                                    style = TextStyle(fontSize = 20.sp),
                                    color = TextNoActive,
                                    text = "${item.respStart} - ${item.respEnd} МСК"
                                )
                                Text(
                                    modifier = Modifier.padding(top = 10.dp),
                                    style = TextStyle(fontSize = 18.sp),
                                    color = TextNoActive,
                                    text = "Респ начнется через ${countTimeBeforeResp(item.timeFromDeath)}"
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    painter = painterResource(id = R.drawable.ic_alarm),
                                    contentDescription = "alarm icon",
                                    tint = White
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(start = 12.dp)
                                ) {
                                    when (alarmsState) {
                                        is AlarmsState.Loading -> renderProgressBar()
                                        is AlarmsState.Error -> renderAlarmError()
                                        is AlarmsState.Content -> {
                                            (alarmsState as AlarmsState.Content)
                                                .alarms[item.name]?.let {
                                                    renderAlarm(
                                                        alarmState = it,
                                                        boss = item.name,
                                                        viewModel = viewModel,
                                                        snackBar = snackBar
                                                    )
                                            }
                                        }
                                    }
                                }
                            }
                    }
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
@Composable
private fun renderProgressBar() {
    CircularProgressIndicator(
        modifier = Modifier.size(20.dp),
        color = White,
        strokeWidth = 2.dp
    )
}

@Composable
private fun renderAlarm(alarmState: OneAlarmState, boss: String, viewModel: MainViewModel, snackBar: SnackbarHostState) {
    val context = LocalContext.current
    val openAlertDialog = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    when {
        openAlertDialog.value -> {
            showAlertDialog(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    val intent = Intent("android.settings.APP_NOTIFICATION_SETTINGS")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra("app_package", context.packageName);
                    intent.putExtra("app_uid", context.applicationInfo.uid);
                    intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName);

                    context.startActivity(intent)
                },
                dialogTitle = "Нет разрешения на отправку уведомлений",
                dialogText = "Чтобы поставить будильник, перейдите в настройки и выдайте разрешение вручную.",
                icon = Icons.Default.Info
            )
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.setAlarm(boss)
        } else {
            scope.launch {
                SharedPreferencesManager.saveBoolean("Notifications", false)
                snackBar.showSnackbar(
                    message = "Ошибка: будильник не может быть установлен без разрешения на отправку уведомлений",
                    duration = SnackbarDuration.Indefinite
                )
            }
        }
    }

    var checked by remember { mutableStateOf(false) }
    var thumbColor by remember { mutableStateOf(Color.Gray) }

    when (alarmState) {
        is OneAlarmState.Loading -> {
            thumbColor = Color.Gray
        }
        is OneAlarmState.Error -> {
            checked = false
            thumbColor = Color.Red
        }
        is OneAlarmState.Content -> {
            if (alarmState.isSet) {
                checked = true
                thumbColor = Color.Green
            } else {
                checked = false
                thumbColor = Color.Gray
            }
        }
    }

    Switch(
        checked = checked,
        colors = SwitchDefaults.colors(
            checkedThumbColor = thumbColor,
            uncheckedThumbColor = thumbColor,
            uncheckedBorderColor = Color.Transparent
        ),
        thumbContent = if (alarmState is OneAlarmState.Loading) {
            {
                renderProgressBar()
            }
        } else {
            null
        },
        onCheckedChange = {

        if (!checked) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                when (PackageManager.PERMISSION_GRANTED) {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) -> {
                        checked = true
                        viewModel.setAlarm(boss)
                    }
                    else  -> {
                        val isThisFirstLaunch = SharedPreferencesManager.getBoolean("Notifications", true)

                        if (isThisFirstLaunch) {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            openAlertDialog.value = true
                        }
                    }
                }
            } else {
                checked = true
                viewModel.setAlarm(boss)
            }
        } else {
            viewModel.deleteAlarm(boss)
        }
    })
}
@Composable
private fun renderAlarmError() {
    Text(text = "Ошибка загрузки", color = Color.Red)
}

@Composable
private fun showAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Перейти в настройки")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Отклонить")
            }
        }
    )
}


private fun countTimeBeforeResp(timeFromDeath: Int): String {
    val hours: Int
    val minutes: Int

    if (timeFromDeath > 1080) {
        hours = (1800 - timeFromDeath) / 60
        minutes = (1800 - timeFromDeath) % 60
    } else {
        hours = (1080 - timeFromDeath) / 60
        minutes = (1080 - timeFromDeath) % 60
    }

    return "$hours часов $minutes минут"
}

private fun countPercentage(timeFromDeath: Int): Float {
    return (1f / 720f) * (timeFromDeath - 1080)
}

private fun convertToTime(timeFromDeath: Int) : String {
    val hours = (timeFromDeath - 1080) / 60
    val minutes = (timeFromDeath - 1080) % 60

    return "$hours : ${
        if (minutes < 10) {
            "0$minutes"
        } else {
            minutes
        }
    }"
}