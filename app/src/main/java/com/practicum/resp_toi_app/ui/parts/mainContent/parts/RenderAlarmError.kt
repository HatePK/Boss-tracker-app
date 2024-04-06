package com.practicum.resp_toi_app.ui.parts.mainContent.parts

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.practicum.resp_toi_app.R

@Composable
fun RenderAlarmError() {
    Text(text = stringResource(id = R.string.card_alarm_error), color = Color.Red)
}