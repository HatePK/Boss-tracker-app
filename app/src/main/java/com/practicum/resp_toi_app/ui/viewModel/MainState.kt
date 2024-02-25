package com.practicum.resp_toi_app.ui.viewModel

import com.practicum.resp_toi_app.domain.entity.BossEntity

sealed interface MainState {
    data object Loading: MainState
    data class Content(val bosses: List<BossEntity>): MainState
    data class Error(val message: String): MainState
}