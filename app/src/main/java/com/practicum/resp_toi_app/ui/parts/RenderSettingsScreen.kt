package com.practicum.resp_toi_app.ui.parts

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.ui.navigation.BottomNavItem
import com.practicum.resp_toi_app.ui.theme.BottomNavColor
import com.practicum.resp_toi_app.ui.theme.CardAttentionColor
import com.practicum.resp_toi_app.ui.theme.CardConfirmedColor
import com.practicum.resp_toi_app.ui.theme.SwitchThumbGreenColor
import com.practicum.resp_toi_app.ui.theme.backgroundCardColor
import com.practicum.resp_toi_app.ui.theme.cardActiveLighterBackground
import com.practicum.resp_toi_app.ui.theme.cardNoActiveAlarmBackground
import com.practicum.resp_toi_app.ui.theme.cardNoActiveBackground
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.theme.progressBarFillColor
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.ui.viewModel.OneAlarmState
import com.practicum.resp_toi_app.ui.viewModel.TestCallState
import com.practicum.resp_toi_app.utils.SharedPreferencesManager
import com.practicum.resp_toi_app.utils.SharedPreferencesManager.COMPACT_MODE
import com.practicum.resp_toi_app.utils.SharedPreferencesManager.XIAOMI_BS
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ForegroundServiceType")
@Composable
fun RenderSettingsScreen(viewModel: MainViewModel, snackBar: SnackbarHostState) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var timerButtonEnabled by remember { mutableStateOf(true) }
    var sheetTimerButtonText by remember { mutableStateOf("Запустить") }
    val context = LocalContext.current as Activity

    var xiaomiBottomSheetShow by remember {
        mutableStateOf(false)
    }

    val timer by viewModel.testCallTimer.collectAsState()
    val testCallState = viewModel.testCallState.collectAsState()

    val interactionSource = remember { MutableInteractionSource() }

    val scope = rememberCoroutineScope()

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            showBottomSheet = true
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                    text = "Я начинающий Android-разработчик и ищу работу! Если у вас есть друзья в этой сфере, я буду благодарен за рекомендацию."
                )
                Text(
                    modifier = Modifier.padding(top = 18.dp),
                    color = Color.White,
                    text = "Спасибо, что ставите оценки в Google Play и на мою страницу в GitHub, это сильно помогает мне в портфолио."
                )
                Row (
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            val openTelegram = Intent(Intent.ACTION_VIEW)
                            openTelegram.setData(Uri.parse("https://github.com/HatePK/Boss-tracker-app"))

                            context.startActivity(openTelegram)
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Github star",
                        tint = Color.Yellow
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically),
                        text = "Поставить звезду на GitHub",
                        style = TextStyle(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        val openAlertDialog = remember { mutableStateOf(false) }

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

        TextButton(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) -> {
                            showBottomSheet = true
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
                    showBottomSheet = true
                }
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 12.dp, top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            var isChecked by remember {
                mutableStateOf(SharedPreferencesManager.getBoolean(COMPACT_MODE, true))
            }

            SharedPreferencesManager.subscribe(COMPACT_MODE) {
                isChecked = SharedPreferencesManager.getBoolean(COMPACT_MODE, true)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_minimize),
                    contentDescription = "send message",
                    modifier = Modifier.padding(start = 2.dp, end = 8.dp),
                    tint = Color.White
                )
                Text(
                    text = "Компактный режим",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.White,
                )
            }
            Switch(
                checked = isChecked,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = backgroundCardColor,
                    checkedTrackColor = cardActiveLighterBackground,
                    uncheckedThumbColor = Color.Gray,
                    uncheckedBorderColor = Color.Transparent
                ),
                thumbContent = if (isChecked) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                            tint = Color.White
                        )
                    }
                } else {
                    null
                },
                onCheckedChange = {
                    SharedPreferencesManager.saveBoolean(COMPACT_MODE, it)
                }
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                val isShowOnLockScreenPermissionEnable = isShowOnLockScreenPermissionEnable(context)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isShowOnLockScreenPermissionEnable != null) {
                        ElevatedCard(
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isShowOnLockScreenPermissionEnable == false) {
                                    CardAttentionColor
                                } else {
                                    CardConfirmedColor
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "warn icon",
                                        tint = if (isShowOnLockScreenPermissionEnable == false) {
                                            Color.Red
                                        } else {
                                            SwitchThumbGreenColor
                                        }
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(0.65f)
                                            .padding(horizontal = 6.dp),
                                        text = if (isShowOnLockScreenPermissionEnable == false) {
                                            "Смартфонам Xiaomi нужно специальное разрешение"
                                        } else {
                                            "Разрешение получено"
                                        },
                                        color = if (isShowOnLockScreenPermissionEnable == false) {
                                            Color.Black
                                        } else {
                                            SwitchThumbGreenColor
                                        },
                                        fontSize = 12.sp,
                                        lineHeight = 13.sp
                                    )
                                }
                                if (isShowOnLockScreenPermissionEnable == false) {
                                    TextButton(
                                        onClick = {
                                            showBottomSheet = false
                                            xiaomiBottomSheetShow = true
                                        },
                                    ) {
                                        Text(text = "Включить")
                                    }
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "check icon",
                                        tint = SwitchThumbGreenColor,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        textAlign = TextAlign.Center,
                        text = "Через 30 секунд после запуска, вам на телефон поступит тестовый будильник. Чтобы убедиться, что всё работает правильно, полностью закройте приложение и заблокируйте экран."
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 20.dp),
                        style = TextStyle(fontSize = 74.sp),
                        text = "00:$timer"
                    )
                    Button(
                        modifier = Modifier.padding(bottom = (WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding().value + 20).dp ),
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

    if (
        xiaomiBottomSheetShow
    ) {
        ModalBottomSheet(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            onDismissRequest = { xiaomiBottomSheetShow = false  },
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
                    modifier = Modifier.padding(
                        top = 30.dp,
                        bottom = (WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding().value + 20).dp
                    ),
                    onClick = {
                        xiaomiBottomSheetShow = false
                        onDisplayPopupPermission(context)
                    }
                ) {
                    Text("Выдать разрешение")
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
        val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
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
