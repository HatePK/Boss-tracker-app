package com.practicum.resp_toi_app.ui.navigation

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val icon: ImageVector? = null, val route: String) {
    data object server: BottomNavItem(route = "main")
    data object settings: BottomNavItem(Icons.Default.Settings,"settings")
}