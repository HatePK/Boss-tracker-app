package com.practicum.resp_toi_app.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.ui.theme.TextNoActive
import com.practicum.resp_toi_app.ui.theme.cardActiveBackground
import com.practicum.resp_toi_app.ui.theme.cardNoActiveColor
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.theme.progressBarBackground
import com.practicum.resp_toi_app.ui.theme.progressBarFillColor
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.PullRefreshState
import eu.bambooapps.material3.pullrefresh.pullRefresh

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderMainContent(data: List<BossEntity>, refreshing: Boolean, pullRefreshState: PullRefreshState) {
    Box() {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
            .background(brush = gradientBackGroundBrush)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(5.dp, vertical = 10.dp),
                    text = "Страница обновляется автоматически раз в минуту. Чтобы обновить принудительно, потяните вверх.",
                    style = TextStyle(fontSize = 14.sp),
                    color = TextNoActive
                )
            }
            items(data) {
                if (it.timeFromDeath > 1080) {
                    val progressPercentage by remember {
                        mutableFloatStateOf(countPercentage(it.timeFromDeath))
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(vertical = 5.dp),
                        elevation = CardDefaults.cardElevation()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(brush = cardActiveBackground)
                                .padding(
                                    start = 20.dp,
                                    end = 12.dp,
                                    top = 14.dp,
                                    bottom = 14.dp
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
                                    text = it.name
                                )
                                Text(
                                    modifier = Modifier.padding(top = 18.dp),
                                    style = TextStyle(fontSize = 16.sp),
                                    color = Color.White,
                                    text = "${it.respStart} - ${it.respEnd} МСК"
                                )
                                Text(
                                    modifier = Modifier.padding(top = 18.dp),
                                    style = TextStyle(fontSize = 18.sp),
                                    color = Color.White,
                                    text = "До конца ${countTimeBeforeResp(it.timeFromDeath)}"
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
                                        text = convertToTime(it.timeFromDeath),
                                        style = TextStyle(
                                            fontSize = 28.sp,
                                            fontWeight = FontWeight.W600
                                        ),
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = cardNoActiveColor,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(bottom = 5.dp)
                    ){
                        Column(modifier = Modifier
                            .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                style = TextStyle(fontSize = 30.sp),
                                color = TextNoActive,
                                text = it.name
                            )
                            Text(
                                modifier = Modifier.padding(top = 10.dp),
                                style = TextStyle(fontSize = 20.sp),
                                color = TextNoActive,
                                text = "${it.respStart} - ${it.respEnd} МСК"
                            )
                            Text(
                                modifier = Modifier.padding(top = 10.dp),
                                style = TextStyle(fontSize = 18.sp),
                                color = TextNoActive,
                                text = "Респ начнется через ${countTimeBeforeResp(it.timeFromDeath)}"
                            )
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