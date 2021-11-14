package com.awonar.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.awonar.android.model.history.Aggregate
import com.awonar.android.model.history.History
import com.awonar.android.shared.domain.history.GetAggregateUseCase
import com.awonar.android.shared.domain.history.GetColumnHistoryUseCase
import com.awonar.android.shared.domain.history.GetHistoryUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getColumnHistoryUseCase: GetColumnHistoryUseCase,
    private val getAggregateUseCase: GetAggregateUseCase,
    private val getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {

    val columnState: StateFlow<List<String>> = flow {
        val data = getColumnHistoryUseCase(Unit).successOr(emptyList())
        emit(data)
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    private val _aggregateState = MutableStateFlow<Aggregate?>(null)
    val aggregateState: StateFlow<Aggregate?> = _aggregateState

    private val _historiesState = MutableStateFlow<PagingData<History>>(PagingData.empty())
    val historiesState: StateFlow<PagingData<History>> = _historiesState

    init {
        val prev7Day = Calendar.getInstance()
        prev7Day.add(Calendar.DATE, -7)
        val timestamp = prev7Day.timeInMillis / 1000
        viewModelScope.launch {
            getAggregateUseCase(timestamp).collect {
                _aggregateState.emit(it.successOr(null))
            }
        }
        viewModelScope.launch {
            getHistoryUseCase(timestamp).collect {
                _historiesState.emit(it.successOr(PagingData.empty()))
            }
        }
    }

    fun getAggregate(timestamp: Long) {
        viewModelScope.launch {
            getAggregateUseCase(timestamp).collect {
                _aggregateState.value = it.successOr(null)
            }
        }
    }

    fun getHistory(timestamp: Long) {
        viewModelScope.launch {
            getHistoryUseCase(timestamp).collect {
                _historiesState.emit(it.successOr(PagingData.empty()))
            }
        }
    }
}