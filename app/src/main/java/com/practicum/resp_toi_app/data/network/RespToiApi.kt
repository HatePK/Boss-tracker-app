package com.practicum.resp_toi_app.data.network

import com.practicum.resp_toi_app.data.dto.Response
import com.practicum.resp_toi_app.data.dto.alarms.AlarmDto
import com.practicum.resp_toi_app.data.dto.getBosses.BossDto
import com.practicum.resp_toi_app.data.dto.getServerList.ServerDto
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RespToiApi {
//    @GET("/status-x1")
//    suspend fun x1Bosses(): ArrayList<BossDto>
//
//    @GET("/status")
//    suspend fun x5Bosses(): ArrayList<BossDto>
//
//    @GET("/status-x1-5")
//    suspend fun x15Bosses(): ArrayList<BossDto>

    @GET("/{route}")
    suspend fun getBosses(@Path("route") route: String): ArrayList<BossDto>

    @GET("/get-server-list")
    suspend fun getServerList(): ArrayList<ServerDto>

    @GET("/get-all-tracks/{id}")
    suspend fun getUserAlarms(@Path("id") userId: String): ArrayList<AlarmDto>

    @POST("/test-call")
    suspend fun testCall(
        @Body callObject: Any
    ): Response

    @POST("/add-new-track")
    suspend fun setAlarm(
        @Body alarm: Any
    ): Response

    @POST("/delete-track")
    suspend fun deleteAlarm(
        @Body alarm: Any
    ): Response
}