package com.practicum.resp_toi_app.domain.api

import com.practicum.resp_toi_app.domain.entity.AlarmEntity
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.domain.entity.ServerEntity
import com.practicum.resp_toi_app.utils.Resource
import kotlinx.coroutines.flow.Flow

interface BossesInteractor {
    fun getBossesInfo(server: ServerEntity): Flow<Pair<List<BossEntity>?, String?>>
    fun getAlarmsInfo(userId: String): Flow<List<AlarmEntity>?>
    fun getServerList(): Flow<List<ServerEntity>?>
    suspend fun setTestCall(userId: String): Resource<String>
    suspend fun setAlarm(userId: String, server: ServerEntity, bossName: String): Resource<String>
    suspend fun deleteAlarm(userId: String, server: ServerEntity, bossName: String): Resource<String>


}