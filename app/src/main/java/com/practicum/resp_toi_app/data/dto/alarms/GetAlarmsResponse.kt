package com.practicum.resp_toi_app.data.dto.alarms

import com.practicum.resp_toi_app.data.dto.Response

data class GetAlarmsResponse(
    val results: ArrayList<AlarmDto>
): Response()
