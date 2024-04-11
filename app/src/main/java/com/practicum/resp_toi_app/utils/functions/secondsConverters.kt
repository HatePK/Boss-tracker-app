package com.practicum.resp_toi_app.utils.functions

import android.content.Context
import com.practicum.resp_toi_app.R

fun countTimeBeforeResp(timeFromDeath: Int, context: Context): String {
    val hours: Int
    val minutes: Int

    if (timeFromDeath > 1080) {
        hours = (1800 - timeFromDeath) / 60
        minutes = (1800 - timeFromDeath) % 60
    } else {
        hours = (1080 - timeFromDeath) / 60
        minutes = (1080 - timeFromDeath) % 60
    }

    return "$hours ${context.applicationContext.getString(R.string.card_hours)} $minutes ${context.applicationContext.getString(R.string.card_mins)}"
}

fun countPercentage(timeFromDeath: Int): Float {
    return (1f / 720f) * (timeFromDeath - 1080)
}

fun countTimeFromRespStarted(
    timeFromDeath: Int,
    textFormat: Boolean,
    context: Context
) : String {
    val hours = (timeFromDeath - 1080) / 60
    val minutes = (timeFromDeath - 1080) % 60

    return if (textFormat) {
        "$hours ${context.applicationContext.getString(R.string.card_hours)} ${
            if (minutes < 10) {
                "0$minutes"
            } else {
                minutes
            }
        } ${context.applicationContext.getString(R.string.card_mins)}"
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