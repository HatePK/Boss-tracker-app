package com.practicum.resp_toi_app.ui.parts.mainContent.parts

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.ui.navigation.BottomNavItem
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.utils.functions.isShowOnLockScreenPermissionEnable
import com.practicum.resp_toi_app.utils.functions.onDisplayPopupPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderXiaomiBottomSheet(
    viewModel: MainViewModel,
    context: Context,
    navController: NavHostController
) {
    val xiaomiBottomSheetState by viewModel.showXiaomiBottomSheet.collectAsState()
    val isShowOnLockScreenEnable = isShowOnLockScreenPermissionEnable(context)

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
                        viewModel.closeXiaomiBottomSheet()
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