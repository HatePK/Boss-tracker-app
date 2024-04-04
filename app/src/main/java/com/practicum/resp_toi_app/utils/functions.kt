package com.practicum.resp_toi_app.utils

import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

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

