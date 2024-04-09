package com.practicum.resp_toi_app.utils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object FormatTimezoneManager {
    private lateinit var utcFormat: SimpleDateFormat
    private lateinit var deviceFormat: SimpleDateFormat

    fun init() {
        utcFormat = SimpleDateFormat("HH:mm", Locale("ru", "RU"))
        deviceFormat = SimpleDateFormat("HH:mm", Locale("ru", "RU"))

        utcFormat.timeZone = TimeZone.getTimeZone("GMT+3")
        deviceFormat.timeZone = TimeZone.getDefault()
    }

    fun countTimeZone(time: String): String {
        val date = utcFormat.parse(time)

        return if (date != null) {
            deviceFormat.format(date)
        } else {
            time
        }
    }
}


