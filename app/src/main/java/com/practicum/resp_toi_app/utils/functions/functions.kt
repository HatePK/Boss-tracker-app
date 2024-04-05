package com.practicum.resp_toi_app.utils.functions

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.practicum.resp_toi_app.utils.SharedPreferencesManager

fun refreshFcmToken() {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            Log.w("ABOBA", "Fetching FCM registration token failed", task.exception)
            return@OnCompleteListener
        }

        val token = task.result
        SharedPreferencesManager.saveString("Token", token)
    })
}

fun isAppAvailable(context: Context, appName: String?): Boolean {
    val pm = context.packageManager
    return try {
        pm.getPackageInfo(appName!!, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

