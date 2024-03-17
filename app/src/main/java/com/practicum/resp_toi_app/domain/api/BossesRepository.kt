package com.practicum.resp_toi_app.domain.api

import com.practicum.resp_toi_app.data.dto.Response
import com.practicum.resp_toi_app.domain.entity.AlarmEntity
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.domain.entity.ServerEntity
import com.practicum.resp_toi_app.utils.Resource
import kotlinx.coroutines.flow.Flow

interface BossesRepository {
    fun getBossesInfo(server: ServerEntity): Flow<Resource<List<BossEntity>>>
    fun getAlarmsInfo(userId: String): Flow<Resource<List<AlarmEntity>>>
    suspend fun setAlarm(userId: String, server: ServerEntity, bossName: String): Response
    suspend fun deleteAlarm(userId: String, server: ServerEntity, bossName: String): Response

}