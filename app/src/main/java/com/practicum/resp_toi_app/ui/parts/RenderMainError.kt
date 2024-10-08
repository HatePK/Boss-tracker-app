package com.practicum.resp_toi_app.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.ui.theme.TextNoActive
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenderMainError(message: String, viewModel: MainViewModel) {

    val refreshing by viewModel.isRefreshing.collectAsState()
    var needToRefresh by remember { mutableStateOf(false) }

    if (needToRefresh) {
        viewModel.refresh()
        needToRefresh = false
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing,
        { viewModel.refresh() }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBackGroundBrush)
                .padding(20.dp)
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                style = TextStyle(fontSize = 18.sp),
                color = TextNoActive
            )
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = { viewModel.refresh() }
            ) {
                Text(text = stringResource(id = R.string.refresh))
            }
        }
        PullRefreshIndicator(
            modifier = Modifier.align(Alignment.TopCenter),
            refreshing = refreshing,
            state = pullRefreshState
        )
    }
}

