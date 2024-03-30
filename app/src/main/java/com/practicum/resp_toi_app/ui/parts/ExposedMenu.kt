package com.practicum.resp_toi_app.ui.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practicum.resp_toi_app.domain.entity.ServerEntity
import com.practicum.resp_toi_app.ui.viewModel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedMenu(
    viewModel: MainViewModel
) {
    val vmServer by viewModel.server.collectAsState()
    val serverList by viewModel.serverList.collectAsState()
    var isMenuExpanded = remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isMenuExpanded.value,
        onExpandedChange = {
            isMenuExpanded.value = !isMenuExpanded.value
        }
    ) {
        val focusRequester = remember { FocusRequester() }
        Row(
            modifier = Modifier
                .menuAnchor()
                .padding(start = 6.dp)
        ) {
            Text(text = vmServer.name, style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500))
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isMenuExpanded.value)
        }

        ExposedDropdownMenu(
            modifier = Modifier
                .focusRequester(focusRequester)
                .width(56.dp),
            expanded = isMenuExpanded.value,
            onDismissRequest = { isMenuExpanded.value = false },

        ) {
            serverList.forEach{ server ->
                DropdownMenuItem(
                    text = {Text(
                        modifier = Modifier.fillMaxWidth(),
                        text=server.name,
                        textAlign = TextAlign.Center
                    )},
                    onClick = {
                        viewModel.setServer(server)
                        isMenuExpanded.value = false
                    }
                )
            }
        }

    }
}