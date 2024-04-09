package com.practicum.resp_toi_app.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.practicum.resp_toi_app.data.api.NetworkClient
import com.practicum.resp_toi_app.data.dto.getBosses.BossesRequest
import com.practicum.resp_toi_app.data.dto.getBosses.BossesResponse
import com.practicum.resp_toi_app.data.dto.Response
import com.practicum.resp_toi_app.data.dto.alarms.DeleteAlarmRequest
import com.practicum.resp_toi_app.data.dto.alarms.GetAlarmsRequest
import com.practicum.resp_toi_app.data.dto.alarms.GetAlarmsResponse
import com.practicum.resp_toi_app.data.dto.alarms.SetAlarmRequest
import com.practicum.resp_toi_app.data.dto.alarms.TestCallRequest
import com.practicum.resp_toi_app.data.dto.getServerList.ServerListRequest
import com.practicum.resp_toi_app.data.dto.getServerList.ServerListResponse
import com.practicum.resp_toi_app.domain.entity.ServerEntity
import java.io.EOFException

class RetrofitNetworkClient(private val context: Context, private val api: RespToiApi): NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto is BossesRequest) {
            return try {
                Log.d("ABOBA", dto.route)
                val response = BossesResponse(api.getBosses(dto.route))
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Log.d("ABOBA", e.message.toString())
                Response().apply { resultCode = 500 }
            }
        }

        if (dto is ServerListRequest) {
            return try {
                val response = ServerListResponse(api.getServerList())
                Log.d("ABOBA", response.serverList.toString())
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Log.d("ABOBA", e.message.toString())
                Response().apply { resultCode = 500 }
            }
        }

        if (dto is TestCallRequest) {
            return try {
                val callObject = object {
                    val userId = dto.userId
                }
                val response = api.testCall(callObject)
                response.apply { resultCode = 201 }
            } catch (e: EOFException) {
                Response().apply { resultCode = 201 }
            } catch (e: Throwable) {
                Log.d("ABOBA", e.toString())
                Response().apply { resultCode = 400 }
            }
        }

        if (dto is GetAlarmsRequest) {
            return try {
                val response = GetAlarmsResponse(api.getUserAlarms(dto.userId))
                Log.d("ABOBA", response.results.toString())
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Log.d("ABOBA", e.message.toString())
                Response().apply { resultCode = 500 }
            }
        }

        if (dto is SetAlarmRequest) {
            return try {
                val alarm = object {
                    val userId = dto.userId
                    val server = dto.server
                    val bossName = dto.boss
                }
                val response = api.setAlarm(alarm)
                response.apply { resultCode = 201 }
            } catch (e: EOFException) {
                Response().apply { resultCode = 201 }
            } catch (e: Throwable) {
                Log.d("ABOBA", e.toString())
                Response().apply { resultCode = 400 }
            }
        }

        if (dto is DeleteAlarmRequest) {
            return try {
                val alarm = object {
                    val userId = dto.userId
                    val server = dto.server
                    val bossName = dto.boss
                }
                val response = api.deleteAlarm(alarm)
                response.apply { resultCode = 201 }
            } catch (e: EOFException) {
                Response().apply { resultCode = 201 }
            } catch (e: Throwable) {
                Log.d("ABOBA", e.toString())
                Response().apply { resultCode = 400 }
            }
        }

        return Response().apply { resultCode = 400 }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}