package com.practicum.resp_toi_app.ui.parts.mainContent.parts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.ui.theme.TextNoActive
import com.practicum.resp_toi_app.ui.theme.cardNoActiveBackground
import com.practicum.resp_toi_app.ui.viewModel.AlarmsState
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.utils.FormatTimezoneManager.countTimeZone
import com.practicum.resp_toi_app.utils.functions.countTimeBeforeResp

@Composable
fun RenderDisabledCardSmall(
    item: BossEntity,
    alarmsState: AlarmsState,
    viewModel: MainViewModel,
    snackBar: SnackbarHostState,
    cardPadding: PaddingValues
) {
    val context = LocalContext.current

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
                    text = "${countTimeZone(item.respStart)} — ${countTimeZone(item.respEnd)}"
                )
                Text(
                    modifier = Modifier.padding(top = 10.dp),
                    style = TextStyle(fontSize = 12.sp),
                    color = TextNoActive,
                    text = "${stringResource(id = R.string.card_resp_begin_after_message)} ${countTimeBeforeResp(item.timeFromDeath, context)}"
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
                    tint = Color.White
                )
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .width(52.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    when (alarmsState) {
                        is AlarmsState.Loading -> RenderProgressBar()
                        is AlarmsState.Error -> RenderAlarmError()
                        is AlarmsState.Content -> {
                            (alarmsState as AlarmsState.Content)
                                .alarms[item.name]?.let {
                                RenderAlarm(
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