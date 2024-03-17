package com.practicum.resp_toi_app.ui.viewModel

sealed interface AlarmsState {
    data object Loading: AlarmsState
    data object Error: AlarmsState
    data class Content(val alarms: MutableMap<String, OneAlarmState>): AlarmsState
}

sealed interface OneAlarmState {
    data object Loading: OneAlarmState
    data object Error: OneAlarmState
    data class Content(val isSet: Boolean): OneAlarmState
}
