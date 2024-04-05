package com.practicum.resp_toi_app.utils.functions

fun countTimeBeforeResp(timeFromDeath: Int): String {
    val hours: Int
    val minutes: Int

    if (timeFromDeath > 1080) {
        hours = (1800 - timeFromDeath) / 60
        minutes = (1800 - timeFromDeath) % 60
    } else {
        hours = (1080 - timeFromDeath) / 60
        minutes = (1080 - timeFromDeath) % 60
    }

    return "$hours часов $minutes минут"
}

fun countPercentage(timeFromDeath: Int): Float {
    return (1f / 720f) * (timeFromDeath - 1080)
}

fun countTimeFromRespStarted(timeFromDeath: Int, textFormat: Boolean) : String {
    val hours = (timeFromDeath - 1080) / 60
    val minutes = (timeFromDeath - 1080) % 60

    return if (textFormat) {
        "$hours часов ${
            if (minutes < 10) {
                "0$minutes"
            } else {
                minutes
            }
        } минут"
    } else {
        "$hours : ${
            if (minutes < 10) {
                "0$minutes"
            } else {
                minutes
            }
        }"
    }
}