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

    init {
        updateInfo()
    }

    fun updateInfo() {
        _stateLiveData.value = MainState.Loading

        viewModelScope.launch {
            bossesInteractor.getBossesInfo(ServerEntity.X1)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
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