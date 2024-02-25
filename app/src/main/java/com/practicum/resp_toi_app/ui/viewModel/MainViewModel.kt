package com.practicum.resp_toi_app.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.resp_toi_app.domain.api.BossesInteractor
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.domain.entity.ServerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val bossesInteractor: BossesInteractor
) : ViewModel() {

    private val _stateLiveData = MutableStateFlow<MainState>(MainState.Loading)
    val stateLiveData: StateFlow<MainState> = _stateLiveData

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private var job: Job? = null

    init {
        getInfo()
    }

    fun refresh() {
        _isRefreshing.value = true

        viewModelScope.launch {
            delay(500)
            loadData()

            _isRefreshing.value = false
        }
    }

    fun getInfo() {
        _stateLiveData.value = MainState.Loading

        viewModelScope.launch {
            delay(1000)
            loadData()
        }
    }

    private suspend fun loadData() {
        job?.cancel()

        job = viewModelScope.launch {
            while (true) {
                bossesInteractor.getBossesInfo(ServerEntity.X1)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }

                delay(60000)
            }
        }
        job?.start()
    }

    private fun processResult(bosses: List<BossEntity>?, message: String?) {
        bosses?.let {
            _stateLiveData.value = MainState.Content(bosses)
        }

        message?.let {
            _stateLiveData.value = MainState.Error(message)
        }
    }

}