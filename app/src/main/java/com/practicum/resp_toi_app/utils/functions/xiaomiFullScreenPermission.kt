package com.practicum.resp_toi_app.utils.functions

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.provider.Settings

fun onDisplayPopupPermission(context: Context) {
    try {
        // MIUI 8
        val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
        localIntent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.PermissionsEditorActivity"
        )
        localIntent.putExtra("extra_pkgname", context.packageName)
        context.startActivity(localIntent)
        return
    } catch (ignore: Exception) {
    }
    try {
        // MIUI 5/6/7
        val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
        localIntent.setClassName(
            "com.miui.securitycenter",
            "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
        )
        localIntent.putExtra("extra_pkgname", context.packageName)
        context.startActivity(localIntent)
        return
    } catch (ignore: Exception) {
    }
    // Otherwise jump to application details
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", context.packageName, null)
    intent.setData(uri)
    context.startActivity(intent)
}

@SuppressLint("DiscouragedPrivateApi")
fun isShowOnLockScreenPermissionEnable(context: Context): Boolean? {
    return try {
        val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val method = AppOpsManager::class.java.getDeclaredMethod(
            "checkOpNoThrow",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType,
            String::class.java
        )
        val result =
            method.invoke(manager, 10020, Binder.getCallingUid(), context.packageName) as Int
        AppOpsManager.MODE_ALLOWED == result
    } catch (e: Exception) {
        null
    }
}