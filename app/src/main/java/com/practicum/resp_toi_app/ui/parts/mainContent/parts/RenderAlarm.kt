package com.practicum.resp_toi_app.ui.parts.mainContent.parts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.practicum.resp_toi_app.ui.theme.SwitchThumbGreenColor
import com.practicum.resp_toi_app.ui.theme.progressBarFillColor
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.ui.viewModel.OneAlarmState
import com.practicum.resp_toi_app.utils.SharedPreferencesManager
import com.practicum.resp_toi_app.utils.functions.moveToNotificationsSettingsIntent
import com.practicum.resp_toi_app.utils.functions.showAlertDialog
import kotlinx.coroutines.launch

@Composable
fun RenderAlarm(
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
                    val intent = moveToNotificationsSettingsIntent(context)
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
    var trackColor by remember { mutableStateOf(Color.White) }

    when (alarmState) {
        is OneAlarmState.Loading -> {
            thumbColor = thumbColor
            trackColor = trackColor
        }
        is OneAlarmState.Error -> {
            checked = false
            thumbColor = Color.Red
            trackColor = Color.White
        }
        is OneAlarmState.Content -> {
            if (alarmState.isSet) {
                checked = true
                thumbColor = SwitchThumbGreenColor
                trackColor = progressBarFillColor
            } else {
                checked = false
                thumbColor = Color.Gray
                trackColor = Color.White
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
                RenderProgressBar()
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
                    val notificationManager = NotificationManagerCompat.from(context)

                    if (notificationManager.areNotificationsEnabled()) {
                        checked = true
                        viewModel.setAlarm(boss)
                    } else {
                        openAlertDialog.value = true
                    }
                }
            } else {
                viewModel.deleteAlarm(boss)
            }
        }
    )
}