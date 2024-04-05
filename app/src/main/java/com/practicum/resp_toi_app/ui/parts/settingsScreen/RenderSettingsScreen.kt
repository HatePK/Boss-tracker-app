package com.practicum.resp_toi_app.ui.parts.settingsScreen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.ui.theme.CardAttentionColor
import com.practicum.resp_toi_app.ui.theme.backgroundCardColor
import com.practicum.resp_toi_app.ui.theme.cardActiveLighterBackground
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.ui.viewModel.TestCallState
import com.practicum.resp_toi_app.utils.SharedPreferencesManager
import com.practicum.resp_toi_app.utils.SharedPreferencesManager.COMPACT_MODE
import com.practicum.resp_toi_app.utils.functions.isShowOnLockScreenPermissionEnable
import com.practicum.resp_toi_app.utils.functions.onDisplayPopupPermission
import com.practicum.resp_toi_app.utils.functions.moveToGithubIntent
import com.practicum.resp_toi_app.utils.functions.moveToNotificationsSettingsIntent
import com.practicum.resp_toi_app.utils.functions.moveToTelegramIntent
import com.practicum.resp_toi_app.utils.functions.showAlertDialog
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderSettingsScreen(viewModel: MainViewModel, snackBar: SnackbarHostState) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var timerButtonEnabled by remember { mutableStateOf(true) }
    var sheetTimerButtonText by remember { mutableStateOf("Запустить") }
    val context = LocalContext.current as Activity
    val timer by viewModel.testCallTimer.collectAsState()
    val testCallState = viewModel.testCallState.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    val scope = rememberCoroutineScope()
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )
    var xiaomiBottomSheetShow by remember {
        mutableStateOf(false)
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            showBottomSheet = true
        } else {
            scope.launch {
                SharedPreferencesManager.saveBoolean("Notifications", false)
                snackBar.showSnackbar(
                    message = context.getString(R.string.error_notifications_disabled_message),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    when (testCallState.value) {
        is TestCallState.Loading -> {
            timerButtonEnabled = false
            sheetTimerButtonText = stringResource(id = R.string.test_call_button_state_loading)
        }
        is TestCallState.Error -> {
            Toast.makeText(
                LocalContext.current,
                stringResource(id = R.string.test_call_server_error),
                Toast.LENGTH_SHORT).show()
            viewModel.errorMessageShown()
        }
        is TestCallState.InProcess -> {
            timerButtonEnabled = false
            sheetTimerButtonText = stringResource(id = R.string.test_call_button_state_close_app)
        }

        is TestCallState.EnabledAfterUse -> {
            sheetTimerButtonText = stringResource(id = R.string.test_call_button_state_try_again)
            timerButtonEnabled = true
        }
        is TestCallState.EnabledFirstTime -> {
            timerButtonEnabled = true
            sheetTimerButtonText = stringResource(id = R.string.test_call_button_state_ready_to_launch)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBackGroundBrush)
            .padding(horizontal = 6.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ElevatedCard(
            colors = CardDefaults.cardColors(containerColor = backgroundCardColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(vertical = 14.dp, horizontal = 14.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.about_header),
                    color = Color.White,
                )
                Text(
                    modifier = Modifier.padding(top = 18.dp),
                    color = Color.White,
                    text = stringResource(id = R.string.about_first),
                )
                Text(
                    modifier = Modifier.padding(top = 18.dp),
                    color = Color.White,
                    text = stringResource(id = R.string.about_second),
                )
                Row (
                    modifier = Modifier
                        .padding(top = 22.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            val openTelegram = moveToGithubIntent()
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
                        text = stringResource(id = R.string.github_link_name),
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
                        val intent = moveToNotificationsSettingsIntent(context)
                        context.startActivity(intent)
                    },
                    dialogTitle = stringResource(id = R.string.alert_dialog_notifications_header),
                    dialogText = stringResource(id = R.string.alert_dialog_notifications_description),
                    icon = Icons.Default.Info
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = backgroundCardColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 6.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            onClick = {
                expandedState = !expandedState
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Box(modifier = Modifier.width(30.dp)) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info icon",
                                modifier = Modifier.padding(end = 6.dp),
                                tint = Color.White
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.settings_menu_button_how_it_works),
                            color = Color.White,
                            modifier = Modifier.padding(top = 1.dp),
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "dropdown",
                        tint = Color.White,
                        modifier = Modifier.rotate(rotationState)
                    )
                }
                
                if (expandedState) {
                    Text(
                        modifier = Modifier.padding(top = 14.dp),
                        text = stringResource(id = R.string.settings_menu_how_it_works_description),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
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
                        } else  -> {
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
                        showBottomSheet = true
                    } else {
                        openAlertDialog.value = true
                    }
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
                        text = stringResource(id = R.string.settings_menu_button_test_call),
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
                val openTelegram = moveToTelegramIntent(context)
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
                        text = stringResource(id = R.string.settings_menu_button_telegram),
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
                    text = stringResource(id = R.string.settings_menu_button_compact_mode),
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
                    if (isShowOnLockScreenPermissionEnable == false) {
                        ElevatedCard(
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = CardAttentionColor
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
                                        tint = Color.Red
                                    )
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(0.65f)
                                            .padding(horizontal = 6.dp),
                                        text = stringResource(id = R.string.xiaomi_permission_alert_header),
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        lineHeight = 13.sp
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        showBottomSheet = false
                                        xiaomiBottomSheetShow = true
                                    },
                                ) {
                                    Text(text = stringResource(id = R.string.xiaomi_permission_alert_button))
                                }
                            }
                        }
                    }
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(id = R.string.test_call_description)
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
                    text = stringResource(id = R.string.xiaomi_permission_explain_header)
                )
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(id = R.string.xiaomi_permission_explain_description)
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
                    Text(stringResource(id = R.string.xiaomi_permission_explain_move_to_settings_button))
                }
            }
        }
    }
}