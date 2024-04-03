package com.practicum.resp_toi_app.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.google.gson.Gson
import com.practicum.resp_toi_app.domain.entity.ServerEntity


object SharedPreferencesManager {

    private const val PREF_NAME = "MySharedPrefs"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gson: Gson

    const val XIAOMI_BS = "Xiaomi bottom sheet"
    const val COMPACT_MODE = "Compact mode"

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        gson = Gson()
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun saveServer(server: ServerEntity) {
        val serverJson = gson.toJson(server)
        sharedPreferences.edit().putString("Server", serverJson).apply()
    }

    fun getServer(): ServerEntity {
        val serverJson = sharedPreferences.getString("Server", "")

        return if (serverJson == "") {
            ServerEntity("x5", "status")
        } else {
            gson.fromJson(serverJson, ServerEntity::class.java)
        }
    }

    fun subscribe(inputKey: String, task: () -> Unit) {
        val listener =
            OnSharedPreferenceChangeListener { _, key ->
                if (key == inputKey) {
                    task()
                }
            }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }
}