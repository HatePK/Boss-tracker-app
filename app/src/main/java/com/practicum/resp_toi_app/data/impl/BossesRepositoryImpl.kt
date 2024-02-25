package com.practicum.resp_toi_app.data.impl

import android.util.Log
import com.practicum.resp_toi_app.data.api.NetworkClient
import com.practicum.resp_toi_app.data.dto.BossesRequest
import com.practicum.resp_toi_app.data.dto.BossesResponse
import com.practicum.resp_toi_app.domain.api.BossesRepository
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
}