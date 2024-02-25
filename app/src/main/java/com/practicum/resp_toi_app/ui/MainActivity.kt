package com.practicum.resp_toi_app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.practicum.resp_toi_app.ui.parts.RenderMainContent
import com.practicum.resp_toi_app.ui.parts.RenderMainError
import com.practicum.resp_toi_app.ui.parts.RenderMainLoading
import com.practicum.resp_toi_app.ui.theme.ResptoiAppTheme
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.viewModel.MainState
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import eu.bambooapps.material3.pullrefresh.PullRefreshState
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResptoiAppTheme {
                Surface(
                    modifier = Modifier
                        .background(brush = gradientBackGroundBrush)
                        .padding(horizontal = 6.dp)
                        .fillMaxSize()
                ) {
                    val state: MainState by vm.stateLiveData.collectAsState()
                    val refreshing by vm.isRefreshing.collectAsState()
                    val pullRefreshState = rememberPullRefreshState(
                        refreshing,
                        { vm.refresh() }
                    )

                    render(state, refreshing, pullRefreshState)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun render(state: MainState, refreshing: Boolean, pullRefreshState: PullRefreshState) {
        when (state) {
            is MainState.Error -> RenderMainError(state.message)
            is MainState.Content -> RenderMainContent(state.bosses, refreshing, pullRefreshState)
            is MainState.Loading -> RenderMainLoading()
        }
    }
}
