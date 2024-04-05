package com.practicum.resp_toi_app.ui.parts

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.practicum.resp_toi_app.ui.parts.mainContent.RenderMainContent
import com.practicum.resp_toi_app.ui.viewModel.MainState
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel, snackBar: SnackbarHostState, navHostController: NavHostController) {

    val state: MainState by viewModel.stateLiveData.collectAsState()

    when (state) {
        is MainState.Error -> RenderMainError((state as MainState.Error).message, viewModel)
        is MainState.Content -> RenderMainContent(
            data = (state as MainState.Content).bosses,
            viewModel = viewModel,
            snackBar = snackBar,
            navController = navHostController
        )
        is MainState.Loading -> RenderMainLoading()
    }
}