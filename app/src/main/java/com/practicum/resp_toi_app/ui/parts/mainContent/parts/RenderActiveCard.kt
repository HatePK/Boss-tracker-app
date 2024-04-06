package com.practicum.resp_toi_app.ui.parts.mainContent.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.ui.theme.backgroundCardColor
import com.practicum.resp_toi_app.ui.theme.cardActiveBackground
import com.practicum.resp_toi_app.ui.theme.progressBarBackground
import com.practicum.resp_toi_app.ui.theme.progressBarFillColor
import com.practicum.resp_toi_app.ui.viewModel.AlarmsState
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.utils.functions.countPercentage
import com.practicum.resp_toi_app.utils.functions.countTimeBeforeResp
import com.practicum.resp_toi_app.utils.functions.countTimeFromRespStarted

@Composable
fun RenderActiveCard(
    item: BossEntity,
    alarmsState: AlarmsState,
    viewModel: MainViewModel,
    snackBar: SnackbarHostState,
    cardPadding: PaddingValues
) {

    val progressPercentage by remember {
        mutableFloatStateOf(countPercentage(item.timeFromDeath))
    }

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = backgroundCardColor,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(cardPadding),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
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
                        color = Color.White,
                        text = item.name
                    )
                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        style = TextStyle(fontSize = 16.sp),
                        color = Color.White,
                        text = "${item.respStart} — ${item.respEnd} МСК"
                    )
                    Text(
                        modifier = Modifier.padding(top = 18.dp),
                        style = TextStyle(fontSize = 16.sp),
                        color = Color.White,
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
                            color = Color.White
                        )
                        Text(
                            modifier = Modifier.padding(top = 3.dp),
                            text = countTimeFromRespStarted(
                                timeFromDeath = item.timeFromDeath,
                                textFormat = false
                            ),
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.W600
                            ),
                            color = Color.White
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
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