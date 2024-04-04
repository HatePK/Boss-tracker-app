package com.practicum.resp_toi_app.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.practicum.resp_toi_app.ui.navigation.BottomNavigationBar
import com.practicum.resp_toi_app.ui.navigation.NavGraph
import com.practicum.resp_toi_app.ui.theme.ResptoiAppTheme
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel
import com.practicum.resp_toi_app.utils.SharedPreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val vm: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResptoiAppTheme {
                Surface(
                    modifier = Modifier
                        .background(brush = gradientBackGroundBrush)
                        .fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    val snackBarHostState = remember { SnackbarHostState() }

                    Scaffold (
                        content = { paddingValues ->
                            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                                NavGraph(
                                    navHostController = navController,
                                    vm,
                                    snackBarHostState
                                )
                            }
                        },
                        bottomBar = { BottomNavigationBar(navController = navController, vm) },
                        snackbarHost = {
                            SnackbarHost(hostState = snackBarHostState)
                        }
                    )
                }
            }
        }
    }
}
