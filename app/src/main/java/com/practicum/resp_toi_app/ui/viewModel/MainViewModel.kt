package com.practicum.resp_toi_app.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.resp_toi_app.domain.api.BossesInteractor
import com.practicum.resp_toi_app.domain.entity.BossEntity
import com.practicum.resp_toi_app.domain.entity.ServerEntity
import com.practicum.resp_toi_app.utils.Resource
import com.practicum.resp_toi_app.utils.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    private val _server = MutableStateFlow(ServerEntity.X5)
    val server: StateFlow<ServerEntity> = _server

    private val _alarmsState = MutableStateFlow<AlarmsState>(AlarmsState.Loading)
    val alarmsState: StateFlow<AlarmsState> = _alarmsState

    private var job: Job? = null

    init {
        getInfo()
    }

    fun getAlarms(bosses: List<BossEntity>) {
        if (alarmsState.value !is AlarmsState.Content) {
            _alarmsState.value = AlarmsState.Loading
        }

        viewModelScope.launch {
            val token = SharedPreferencesManager.getString("Token", "")

            delay(500)

            val map = mutableMapOf<String, OneAlarmState>()

            bosses.forEach{
                map[it.name] = OneAlarmState.Loading
            }

            bossesInteractor.getAlarmsInfo(token).collect {alarms ->
                if (alarms != null) {

                    Log.d("ABOBA", alarms.toString())

                    map.forEach{
                        map[it.key] = OneAlarmState.Content(false)
                    }

                    alarms.filter{it.server == server.value}.forEach{
                        map[it.bossName] = OneAlarmState.Content(true)
                    }

                    _alarmsState.value = AlarmsState.Content(map)
                } else {
                    _alarmsState.value = AlarmsState.Error
                }
            }
        }
    }

    fun setServer(serverEntity: ServerEntity) {
        _server.value = serverEntity
        _alarmsState.value = AlarmsState.Loading
        getInfo()
    }

    fun setAlarm(boss: String) {
        val token = SharedPreferencesManager.getString("Token", "")

        val newSet = (_alarmsState.value as AlarmsState.Content).alarms.toMutableMap()
        newSet[boss] = OneAlarmState.Loading

        _alarmsState.value = AlarmsState.Content(newSet)

        viewModelScope.launch {
            delay(1000)

            val response = bossesInteractor.setAlarm(
                userId = token,
                server = server.value,
                bossName = boss
            )

            val dataSet = newSet.toMutableMap()

            when (response) {
                is Resource.Success -> {
                    dataSet[boss] = OneAlarmState.Content(true)
                }
                is Resource.Error -> {
                    dataSet[boss] = OneAlarmState.Error
                }
            }

            _alarmsState.value = AlarmsState.Content(dataSet)
        }
    }

    fun deleteAlarm(boss: String) {
        val token = SharedPreferencesManager.getString("Token", "")

        val newSet = (_alarmsState.value as AlarmsState.Content).alarms.toMutableMap()
        newSet[boss] = OneAlarmState.Loading

        _alarmsState.value = AlarmsState.Content(newSet)

        viewModelScope.launch {
            delay(1000)

            val response = bossesInteractor.deleteAlarm(
                userId = token,
                server = server.value,
                bossName = boss
            )

            val dataSet = newSet.toMutableMap()

            when (response) {
                is Resource.Success -> {
                    dataSet[boss] = OneAlarmState.Content(false)
                }

                is Resource.Error -> {
                    dataSet[boss] = OneAlarmState.Error
                }
            }

            _alarmsState.value = AlarmsState.Content(dataSet)
        }
    }

    fun refresh() {
        _isRefreshing.value = true
        _alarmsState.value = AlarmsState.Loading

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
                bossesInteractor.getBossesInfo(server.value)
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
            getAlarms(bosses)
        }

        message?.let {
            _stateLiveData.value = MainState.Error(message)
        }
    }

}