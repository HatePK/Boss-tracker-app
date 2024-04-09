package com.practicum.resp_toi_app.ui.parts.mainContent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.ui.parts.mainContent.parts.RenderActiveCard
import com.practicum.resp_toi_app.ui.parts.mainContent.parts.RenderActiveCardSmall
import com.practicum.resp_toi_app.ui.parts.mainContent.parts.RenderDisabledCard
import com.practicum.resp_toi_app.ui.parts.mainContent.parts.RenderDisabledCardSmall
import com.practicum.resp_toi_app.ui.parts.mainContent.parts.RenderXiaomiBottomSheet
import com.practicum.resp_toi_app.ui.theme.TextNoActive
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.viewModel.AlarmsState
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.utils.SharedPreferencesManager
import com.practicum.resp_toi_app.utils.SharedPreferencesManager.COMPACT_MODE
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderMainContent(
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
    val context = LocalContext.current

    Box {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBackGroundBrush)
            .padding(horizontal = 6.dp)
            .pullRefresh(pullRefreshState)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(5.dp, top = 10.dp, bottom = 6.dp),
                    text = stringResource(id = R.string.main_comment),
                    style = TextStyle(fontSize = 14.sp),
                    color = TextNoActive
                )
                Text(
                    modifier = Modifier.padding(5.dp, bottom = 10.dp),
                    text = "${stringResource(id = R.string.main_comment_timezone)} ${TimeZone.getDefault().id}",
                    style = TextStyle(fontSize = 14.sp),
                    color = TextNoActive
                )
            }

            if (SharedPreferencesManager.getBoolean(COMPACT_MODE, true)) {
                itemsIndexed(data) {index, item ->
                    val cardPadding = if (index == data.lastIndex) {
                        PaddingValues(bottom = 16.dp)
                    } else {
                        PaddingValues(bottom = 8.dp)
                    }

                    if (item.timeFromDeath > 1080) {
                        RenderActiveCardSmall(
                            item = item,
                            alarmsState = alarmsState,
                            viewModel = viewModel,
                            snackBar = snackBar,
                            cardPadding = cardPadding
                        )
                    } else {
                        RenderDisabledCardSmall(
                            item = item,
                            alarmsState = alarmsState,
                            viewModel = viewModel,
                            snackBar = snackBar,
                            cardPadding = cardPadding
                        )
                    }
                }
            } else {
                itemsIndexed(data) {index, item ->
                    val cardPadding = if (index == data.lastIndex) {
                        PaddingValues(bottom = 16.dp)
                    } else {
                        PaddingValues(bottom = 8.dp)
                    }

                    if (item.timeFromDeath > 1080) {
                        RenderActiveCard(
                            item = item,
                            alarmsState = alarmsState,
                            viewModel = viewModel,
                            snackBar = snackBar,
                            cardPadding = cardPadding
                        )
                    } else {
                        RenderDisabledCard(
                            item = item,
                            alarmsState = alarmsState,
                            viewModel = viewModel,
                            snackBar = snackBar,
                            cardPadding = cardPadding
                        )
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        RenderXiaomiBottomSheet(
            viewModel = viewModel,
            context = context,
            navController = navController
        )
    }
}






