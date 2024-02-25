package com.practicum.resp_toi_app.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.practicum.resp_toi_app.data.api.NetworkClient
import com.practicum.resp_toi_app.data.dto.BossesRequest
import com.practicum.resp_toi_app.data.dto.BossesResponse
import com.practicum.resp_toi_app.data.dto.Response
import com.practicum.resp_toi_app.domain.entity.ServerEntity

class RetrofitNetworkClient(private val context: Context, private val api: RespToiApi): NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        if (dto is BossesRequest) {
            return try {
                val response  = when (dto.server) {
                    ServerEntity.X1 -> BossesResponse(api.x1Bosses())
                    ServerEntity.X15 -> BossesResponse(api.x15Bosses())
                    ServerEntity.X5 -> BossesResponse(api.x5Bosses())
                    ServerEntity.X7 -> BossesResponse(api.x7Bosses())
                }
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Log.d("ABOBA", e.message.toString())
                Response().apply { resultCode = 500 }
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