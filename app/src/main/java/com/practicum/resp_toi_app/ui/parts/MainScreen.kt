package com.practicum.resp_toi_app.ui.parts

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.practicum.resp_toi_app.ui.viewModel.MainState
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@Composable
fun MainScreen(viewModel: MainViewModel, snackBar: SnackbarHostState, navHostController: NavHostController) {

    val state: MainState by viewModel.stateLiveData.collectAsState()

    when (state) {
        is MainState.Error -> RenderMainError((state as MainState.Error).message, viewModel)
        is MainState.Content -> RenderMainContentExperemental((state as MainState.Content).bosses, viewModel, snackBar, navHostController)
        is MainState.Loading -> RenderMainLoading()
    }
}