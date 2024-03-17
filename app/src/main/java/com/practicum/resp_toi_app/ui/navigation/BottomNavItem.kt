package com.practicum.resp_toi_app.ui.navigation

import com.practicum.resp_toi_app.R

sealed class BottomNavItem(val label: String, val icon: Int? = null, val route: String) {
    data object server: BottomNavItem(label = "Сервер", route = "main")
    data object settings: BottomNavItem("Настройки", R.drawable.baseline_construction_24,"settings")
}