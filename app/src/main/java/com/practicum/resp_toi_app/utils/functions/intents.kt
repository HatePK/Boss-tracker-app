package com.practicum.resp_toi_app.utils.functions

import android.content.Context
import android.content.Intent
import android.net.Uri

fun moveToNotificationsSettingsIntent(context: Context): Intent {
    return Intent("android.settings.APP_NOTIFICATION_SETTINGS").apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        putExtra("app_package", context.packageName);
        putExtra("app_uid", context.applicationInfo.uid);
        putExtra("android.provider.extra.APP_PACKAGE", context.packageName);
    }
}

fun moveToTelegramIntent(context: Context): Intent {
    return Intent(Intent.ACTION_VIEW).apply {
        val appName = "org.telegram.messenger"
        data = Uri.parse("https://t.me/dima_ret")

        if (isAppAvailable(context, appName)) {
            `package` = appName
        }
    }
}

fun moveToGithubIntent(): Intent {
    return Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://github.com/HatePK/Boss-tracker-app")
    }
}