package com.practicum.resp_toi_app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val progressBarFillColor = Color(0xBB9AFF88)
val progressBarBackground = Color(0x6DD8DBD8)
val cardNoActiveBackground = Color.Black.copy(alpha = 0.2f)
val backgroundCardColor = Color(0xFF6AA2E9).copy(alpha = 0.3f)
val TextNoActive = Color(0xFFA2A6B1)
val SwitchThumbGreenColor = Color(0xFF2C722F)
val BottomNavColor = Color(0xFF1F3753)
val ThemeColor = Color(0xFF283E63)
val ProgressBarColor = Color(0xFF92ACD8)

val gradientBackGroundBrush = Brush.verticalGradient(
    colors = listOf(Color(0xFF283E63), Color(0xFF1F3753)),
    startY = 0f,
    endY = Float.POSITIVE_INFINITY
)

val cardActiveBackground = Brush.horizontalGradient(
    colors = listOf(Color(4283198374), Color(4283143870)),
    startX = 0f,
    endX = Float.POSITIVE_INFINITY
)

