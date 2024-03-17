package com.practicum.resp_toi_app.ui.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicum.resp_toi_app.ui.parts.MainScreen
import com.practicum.resp_toi_app.ui.parts.RenderSettingsScreen
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel

@Composable
fun NavGraph(
    navHostController: NavHostController,
    viewModel: MainViewModel,
    snackBarHostState: SnackbarHostState
) {
    NavHost(navController = navHostController, startDestination = "main") {
        composable("main") {
            MainScreen(viewModel = viewModel, snackBar = snackBarHostState)
        }
        composable("settings") {
            RenderSettingsScreen()
        }
    }
}