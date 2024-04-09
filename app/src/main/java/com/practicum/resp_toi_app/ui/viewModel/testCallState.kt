package com.practicum.resp_toi_app.ui.viewModel

sealed interface TestCallState {
    data object EnabledFirstTime: TestCallState
    data object EnabledAfterUse: TestCallState
    data object Loading: TestCallState
    data class Error(val message: String?) : TestCallState
    data object InProcess: TestCallState
}