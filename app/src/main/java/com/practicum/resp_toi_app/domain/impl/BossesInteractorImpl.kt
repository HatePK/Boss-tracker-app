package com.practicum.resp_toi_app.domain.impl

import android.util.Log
import com.practicum.resp_toi_app.domain.api.BossesInteractor
import com.practicum.resp_toi_app.domain.api.BossesRepository
import com.practicum.resp_toi_app.domain.entity.AlarmEntity
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.domain.entity.ServerEntity
import com.practicum.resp_toi_app.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BossesInteractorImpl(
    private val bossesRepository: BossesRepository
): BossesInteractor {
    override fun getBossesInfo(server: ServerEntity): Flow<Pair<List<BossEntity>?, String?>> {
        return bossesRepository.getBossesInfo(server).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun getAlarmsInfo(userId: String): Flow<List<AlarmEntity>?> {
        return bossesRepository.getAlarmsInfo(userId).map { result ->
            when (result) {
                is Resource.Success -> {
                    result.data
                }
                is Resource.Error -> {
                    null
                }
            }
        }
    }

    override fun getServerList(): Flow<List<ServerEntity>?> {
        return bossesRepository.getServerList().map { result ->
            when (result) {
                is Resource.Success -> {
                    result.data
                }
                is Resource.Error -> {
                    null
                }
            }
        }
    }

    override suspend fun setTestCall(userId: String): Resource<String> {
        val response = bossesRepository.setTestCall(userId)
        Log.d("ABOBA", response.resultCode.toString())

        return when (response.resultCode) {
            201 -> Resource.Success()
            else -> Resource.Error("Ошибка при обработке запроса")
        }
    }

    override suspend fun setAlarm(userId: String, server: ServerEntity, bossName: String): Resource<String> {
        val response = bossesRepository.setAlarm(userId, server, bossName)
        Log.d("ABOBA", response.resultCode.toString())

        return when (response.resultCode) {
            201 -> Resource.Success()
            else -> Resource.Error("Ошибка при обработке запроса")
        }
    }

    override suspend fun deleteAlarm(userId: String, server: ServerEntity, bossName: String): Resource<String> {
        val response = bossesRepository.deleteAlarm(userId, server, bossName)

        return when (response.resultCode) {
            201 -> Resource.Success()
            else -> Resource.Error("Ошибка при обработке запроса")
        }
    }
}