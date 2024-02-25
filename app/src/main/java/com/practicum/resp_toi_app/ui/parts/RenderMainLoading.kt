package com.practicum.resp_toi_app.ui.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.practicum.resp_toi_app.ui.theme.gradientBackGroundBrush

@Composable
fun RenderMainLoading() {
    Box(
        modifier = Modifier.fillMaxSize().background(brush = gradientBackGroundBrush),
        contentAlignment = Alignment.Center
        ) {
        CircularProgressIndicator(modifier = Modifier.size(40.dp))
    }
}