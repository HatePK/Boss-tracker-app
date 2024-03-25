package com.practicum.resp_toi_app.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.practicum.resp_toi_app.ui.parts.ExposedMenu
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel


@Composable
fun BottomNavigationBar(navController: NavController, viewModel: MainViewModel) {

    val vmServer by viewModel.server.collectAsState()

    val menuItems = listOf(
        BottomNavItem.server,
        BottomNavItem.settings
    )

    NavigationBar(
        Modifier.background(Color.Yellow)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        var isMenuExpanded = remember {
            mutableStateOf(false)
        }

        menuItems.forEach{ item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White
                ),
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) navController.navigate(item.route)
                },
                icon = {
                    if (item.icon != null) {
                        Icon(painter = painterResource(id = item.icon), contentDescription = "menu icon")
                    } else {
                        if (currentRoute == "main") {
                            ExposedMenu(isMenuExpanded, viewModel)
                        } else {
                            Text(text = vmServer.name, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500))
                        }
                    }},
                label = {
                    Text(text = item.label)
                }
            )
        }
    }

}
