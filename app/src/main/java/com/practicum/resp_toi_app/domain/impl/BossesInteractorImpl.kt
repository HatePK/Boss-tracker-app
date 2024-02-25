package com.practicum.resp_toi_app.domain.impl

import com.practicum.resp_toi_app.domain.api.BossesInteractor
import com.practicum.resp_toi_app.domain.api.BossesRepository
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
}