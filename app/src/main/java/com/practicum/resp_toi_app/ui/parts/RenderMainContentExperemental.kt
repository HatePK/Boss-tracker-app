package com.practicum.resp_toi_app.ui.parts

import android.Manifest
import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Context.APP_OPS_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.ui.navigation.BottomNavItem
import com.practicum.resp_toi_app.ui.theme.ProgressBarColor
import com.practicum.resp_toi_app.ui.theme.SwitchThumbGreenColor
import com.practicum.resp_toi_app.ui.theme.TextNoActive
import com.practicum.resp_toi_app.ui.theme.backgroundCardColor
import com.practicum.resp_toi_app.ui.theme.cardActiveBackground
import com.practicum.resp_toi_app.ui.theme.cardActiveLighterBackground
import com.practicum.resp_toi_app.ui.theme.cardNoActiveAlarmBackground
import com.practicum.resp_toi_app.ui.theme.cardNoActiveBackground
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.theme.progressBarBackground
import com.practicum.resp_toi_app.ui.theme.progressBarFillColor
import com.practicum.resp_toi_app.ui.viewModel.AlarmsState
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.ui.viewModel.OneAlarmState
import com.practicum.resp_toi_app.utils.SharedPreferencesManager
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderMainContentExperemental(
    data: List<BossEntity>,
    viewModel: MainViewModel,
    snackBar: SnackbarHostState,
    navController: NavHostController
) {
    val refreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing,
        { viewModel.refresh() }
    )

    val alarmsState: AlarmsState by viewModel.alarmsState.collectAsState()
    val xiaomiBottomSheetState by viewModel.showXiaomiBottomSheet.collectAsState()
    val context = LocalContext.current
    val isShowOnLockScreenEnable = isShowOnLockScreenPermissionEnable(context)

    Box {
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
                    style = TextStyle(fontSize = 12.sp),
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
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(cardPadding),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = cardActiveLighterBackground
                        )
                    ) {
                        Column(
                            modifier = Modifier.background(brush = cardActiveBackground)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        end = 12.dp,
                                        top = 24.dp,
                                        bottom = 24.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(0.3f),
                                ) {
                                    Text(
                                        style = TextStyle(fontSize = 22.sp),
                                        color = White,
                                        text = item.name.uppercase()
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        style = TextStyle(fontSize = 14.sp),
                                        color = White,
                                        text = "${item.respStart} — ${item.respEnd} МСК"
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        style = TextStyle(fontSize = 12.sp),
                                        color = White,
                                        text = "Респ идет уже ${convertToTime(item.timeFromDeath)}"
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .height(50.dp)
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
                                    Row(
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                            .width(52.dp),
                                        horizontalArrangement = Arrangement.Center
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
                            LinearProgressIndicator(
                                progress = progressPercentage,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(5.dp),
                                color = progressBarFillColor,
                                trackColor = progressBarBackground
                            )
                        }
                    }
                } else {
                    Column {
                        ElevatedCard(
                            colors = CardDefaults.cardColors(containerColor = cardNoActiveBackground),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(cardPadding),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 6.dp
                            )
                        ){
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        end = 12.dp,
                                        top = 24.dp,
                                        bottom = 24.dp
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Column(modifier = Modifier.fillMaxWidth(0.65f)) {
                                    Text(
                                        style = TextStyle(fontSize = 22.sp),
                                        color = TextNoActive,
                                        text = item.name.uppercase()
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        style = TextStyle(fontSize = 14.sp),
                                        color = TextNoActive,
                                        text = "${item.respStart} — ${item.respEnd} МСК"
                                    )
                                    Text(
                                        modifier = Modifier.padding(top = 10.dp),
                                        style = TextStyle(fontSize = 12.sp),
                                        color = TextNoActive,
                                        text = "Респ начнется через ${countTimeBeforeResp(item.timeFromDeath)}"
                                    )
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Icon(
                                        modifier = Modifier.size(28.dp),
                                        painter = painterResource(id = R.drawable.ic_alarm),
                                        contentDescription = "alarm icon",
                                        tint = White
                                    )
                                    Row(
                                        modifier = Modifier
                                            .padding(start = 12.dp)
                                            .width(52.dp),
                                        horizontalArrangement = Arrangement.Center
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
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        if (xiaomiBottomSheetState && isShowOnLockScreenEnable == false) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { viewModel.closeXiaomiBottomSheet() },
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        style = TextStyle(fontSize = 24.sp),
                        text = "Кажется, у вас Xiaomi"
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        textAlign = TextAlign.Center,
                        text = "Будильник может не отображаться на заблокированном экране. Включите разрешение «Экран блокировки», чтобы всё работало как надо."
                    )

                    ElevatedCard(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        )
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth(),
                            painter = painterResource(R.drawable.screenshot),
                            contentDescription = "screen",
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    Button(
                        modifier = Modifier.padding(vertical = 10.dp),
                        onClick = {
                            onDisplayPopupPermission(context)
                        }
                    ) {
                        Text("Выдать разрешение")
                    }
                    Text(text = "или")
                    TextButton(
                        onClick = {
                            viewModel.closeXiaomiBottomSheet()
                            navController.navigate(BottomNavItem.settings.route)
                        }
                    ) {
                        Text(
                            text = "Протестировать будильник"
                        )
                    }
                    TextButton(
                        modifier = Modifier.padding(top = 30.dp, bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                        onClick = {
                            viewModel.stopShowXiaomiBottomSheet()
                        }
                    ) {
                        Text(
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.Underline,
                            text = "Больше не показывать это сообщение"
                        )
                    }
                }
            }
        }
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
private fun renderAlarm(
    alarmState: OneAlarmState,
    boss: String,
    viewModel: MainViewModel,
    snackBar: SnackbarHostState
) {
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
                dialogText = "Чтобы поставить будильник, перейдите в настройки и выдайте разрешение.",
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
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    var checked by remember { mutableStateOf(false) }
    var thumbColor by remember { mutableStateOf(Color.Gray) }
    var trackColor by remember { mutableStateOf(White) }

    when (alarmState) {
        is OneAlarmState.Loading -> {
            thumbColor = thumbColor
            trackColor = trackColor
        }
        is OneAlarmState.Error -> {
            checked = false
            thumbColor = Color.Red
            trackColor = White
        }
        is OneAlarmState.Content -> {
            if (alarmState.isSet) {
                checked = true
                thumbColor = SwitchThumbGreenColor
                trackColor = progressBarFillColor
            } else {
                checked = false
                thumbColor = Color.Gray
                trackColor = White
            }
        }
    }

    Switch(
        checked = checked,
        colors = SwitchDefaults.colors(
            checkedThumbColor = thumbColor,
            uncheckedThumbColor = thumbColor,
            uncheckedBorderColor = Color.Transparent,
            checkedTrackColor = trackColor
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
        }
    )
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

    return "$hours часов ${
        if (minutes < 10) {
            "0$minutes"
        } else {
            minutes
        }
    } минут"
}

private fun onDisplayPopupPermission(context: Context) {
    try {
        // MIUI 8
        val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
        localIntent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.PermissionsEditorActivity"
        )
        localIntent.putExtra("extra_pkgname", context.packageName)
        context.startActivity(localIntent)
        return
    } catch (ignore: Exception) {
    }
    try {
        // MIUI 5/6/7
        val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
        localIntent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
        )
        localIntent.putExtra("extra_pkgname", context.packageName)
        context.startActivity(localIntent)
        return
    } catch (ignore: Exception) {
    }
    // Otherwise jump to application details
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", context.packageName, null)
    intent.setData(uri)
    context.startActivity(intent)
}

@SuppressLint("DiscouragedPrivateApi")
private fun isShowOnLockScreenPermissionEnable(context: Context): Boolean? {
    return try {
        val manager = context.getSystemService(APP_OPS_SERVICE) as AppOpsManager
        val method = AppOpsManager::class.java.getDeclaredMethod(
            "checkOpNoThrow",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType,
            String::class.java
        )
        val result =
            method.invoke(manager, 10020, Binder.getCallingUid(), context.packageName) as Int
        AppOpsManager.MODE_ALLOWED == result
    } catch (e: Exception) {
        null
    }
}