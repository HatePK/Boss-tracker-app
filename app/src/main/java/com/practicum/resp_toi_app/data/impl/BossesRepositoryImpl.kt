package com.practicum.resp_toi_app.data.impl

import com.practicum.resp_toi_app.data.api.NetworkClient
import com.practicum.resp_toi_app.data.dto.Response
import com.practicum.resp_toi_app.data.dto.alarms.DeleteAlarmRequest
import com.practicum.resp_toi_app.data.dto.alarms.GetAlarmsRequest
import com.practicum.resp_toi_app.data.dto.alarms.GetAlarmsResponse
import com.practicum.resp_toi_app.data.dto.alarms.SetAlarmRequest
import com.practicum.resp_toi_app.data.dto.getBosses.BossesRequest
import com.practicum.resp_toi_app.data.dto.getBosses.BossesResponse
import com.practicum.resp_toi_app.domain.api.BossesRepository
import com.practicum.resp_toi_app.domain.entity.AlarmEntity
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.domain.entity.ServerEntity
import com.practicum.resp_toi_app.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BossesRepositoryImpl(
    private val networkClient: NetworkClient
): BossesRepository {
    override fun getBossesInfo(server: ServerEntity): Flow<Resource<List<BossEntity>>> = flow {
        val response = networkClient.doRequest(BossesRequest(server))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                emit(Resource.Success((response as BossesResponse).results.map {
                    BossEntity(
                        it.name,
                        it.respStart,
                        it.respEnd,
                        it.timeFromDeath
                    )
                }))
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun getAlarmsInfo(userId: String): Flow<Resource<List<AlarmEntity>>> = flow {
        val response = networkClient.doRequest(GetAlarmsRequest(userId))

        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            200 -> {
                emit(Resource.Success((response as GetAlarmsResponse).results.map {
                    AlarmEntity(
                        bossName = it.boss,
                        server = when (it.server) {
                            ServerEntity.X1.name -> ServerEntity.X1
                            ServerEntity.X15.name -> ServerEntity.X15
                            ServerEntity.X5.name -> ServerEntity.X5
                            else -> ServerEntity.X5
                        }
                    )
                }))
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override suspend fun setAlarm(userId: String, server: ServerEntity, bossName: String): Response {
        return networkClient.doRequest(SetAlarmRequest(userId, server.name, bossName))
    }

    override suspend fun deleteAlarm(userId: String, server: ServerEntity, bossName: String): Response {
        return networkClient.doRequest(DeleteAlarmRequest(userId, server.name, bossName))
    }
}