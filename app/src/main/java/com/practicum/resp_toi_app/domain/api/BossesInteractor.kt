package com.practicum.resp_toi_app.domain.api

import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.domain.entity.ServerEntity
import kotlinx.coroutines.flow.Flow

interface BossesInteractor {
    fun getBossesInfo(server: ServerEntity): Flow<Pair<List<BossEntity>?, String?>>
}