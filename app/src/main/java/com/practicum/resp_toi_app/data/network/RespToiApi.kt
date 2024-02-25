package com.practicum.resp_toi_app.data.network

import com.practicum.resp_toi_app.data.dto.BossDto
import com.practicum.resp_toi_app.data.dto.BossesResponse
import retrofit2.http.GET

interface RespToiApi {
    @GET("/status-x1")
    suspend fun x1Bosses(): ArrayList<BossDto>

    @GET("/status")
    suspend fun x5Bosses(): ArrayList<BossDto>

    @GET("/status-x7")
    suspend fun x7Bosses(): ArrayList<BossDto>

    @GET("/status-x1-5")
    suspend fun x15Bosses(): ArrayList<BossDto>
}