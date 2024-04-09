package com.practicum.resp_toi_app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val label: String, val icon: ImageVector? = null, val route: String) {
    data object server: BottomNavItem(label = "Сервер", route = "main")
    data object settings: BottomNavItem("Настройки", Icons.Default.Settings,"settings")
}