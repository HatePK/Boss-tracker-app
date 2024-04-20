package com.practicum.resp_toi_app.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.practicum.resp_toi_app.R
import com.practicum.resp_toi_app.ui.parts.ExposedMenu
import com.practicum.resp_toi_app.ui.theme.BottomNavColor
import com.practicum.resp_toi_app.ui.theme.TextNoActive
import com.practicum.resp_toi_app.ui.theme.ThemeColor
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel


@Composable
fun BottomNavigationBar(navController: NavController, viewModel: MainViewModel) {

    val vmServer by viewModel.server.collectAsState()

    val menuItems = listOf(
        BottomNavItem.server,
        BottomNavItem.settings
    )

    NavigationBar(
        containerColor = BottomNavColor,
        modifier = Modifier
            .background(ThemeColor)
            .padding(top = 1.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        menuItems.forEach{ item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = TextNoActive,
                    unselectedTextColor = TextNoActive,
                    indicatorColor = BottomNavColor
                ),
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) navController.navigate(item.route)
                },
                icon = {
                    if (item.icon != null) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "menu icon",
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        if (currentRoute == "main") {
                            ExposedMenu(viewModel)
                        } else {
                            Text(text = vmServer.name, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500))
                        }
                    }},
                label = {
                    when (item) {
                        is BottomNavItem.server -> Text(text = stringResource(id = R.string.server))
                        is BottomNavItem.settings -> Text(text = stringResource(id = R.string.settings))
                    }
                }
            )
        }
    }

}
