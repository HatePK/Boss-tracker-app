package com.practicum.resp_toi_app.domain.api

import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.domain.entity.ServerEntity
import com.practicum.resp_toi_app.utils.Resource
import kotlinx.coroutines.flow.Flow

interface BossesRepository {
    fun getBossesInfo(server: ServerEntity): Flow<Resource<List<BossEntity>>>
}