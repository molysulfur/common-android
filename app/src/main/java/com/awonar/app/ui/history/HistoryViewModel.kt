package com.awonar.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.history.Aggregate
import com.awonar.android.model.history.History
import com.awonar.android.model.history.HistoryRequest
import com.awonar.android.shared.domain.history.GetAggregateUseCase
import com.awonar.android.shared.domain.history.GetHistoryUseCase
import com.awonar.android.shared.domain.history.GetMarketHistoryUseCase
import com.awonar.app.ui.history.adapter.HistoryItem
import com.awonar.app.ui.history.adapter.HistoryType
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAggregateUseCase: GetAggregateUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val getMarketHistoryUseCase: GetMarketHistoryUseCase
) : ViewModel() {

    private val _aggregateState = MutableStateFlow<Aggregate?>(null)
    val aggregateState: StateFlow<Aggregate?> = _aggregateState

    private val _historiesState = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historiesState: StateFlow<List<HistoryItem>> = _historiesState

    private val _historyDetailState = MutableStateFlow<History?>(null)
    val historyDetailState: StateFlow<History?> = _historyDetailState

    private var timeStamp: MutableStateFlow<Long>

    init {
        val prev7Day = Calendar.getInstance()
        prev7Day.add(Calendar.DATE, -7)
        timeStamp = MutableStateFlow(prev7Day.timeInMillis / 1000)

        viewModelScope.launch {
            getAggregate(timeStamp.value)
        }

        getHistory(timestamp = timeStamp.value)
    }

    fun getAggregate(timestamp: Long) {
        viewModelScope.launch {
            getAggregateUseCase(timestamp).collect {
                _aggregateState.value = it.successOr(null)
            }
        }
    }

    fun getHistory(page: Int = 1) {
        viewModelScope.launch {
            getHistoryUseCase(HistoryRequest(timeStamp.value, page)).collect { result ->
                val data = result.successOr(null)
                val itemList: MutableList<HistoryItem> = data?.history?.map {
                    HistoryItem.ManualItem(it)
                }?.toMutableList() ?: mutableListOf()
                val newPage = data?.page ?: 0
                if (newPage > 0) {
                    itemList.add(HistoryItem.LoadMoreItem(newPage))
                }
                addHistory(itemList)
            }
        }
    }

    fun getHistory(timestamp: Long) {
        viewModelScope.launch {
            _historiesState.emit(emptyList())
            timeStamp.emit(timestamp)
            getHistoryUseCase(HistoryRequest(timeStamp.value)).collect {
                val result = it.successOr(null)
                val itemList: MutableList<HistoryItem> = result?.history?.map {
                    HistoryItem.ManualItem(it)
                }?.toMutableList() ?: mutableListOf()
                val page = result?.page ?: 0
                if (page > 0) {
                    itemList.add(HistoryItem.LoadMoreItem(page))
                }
                _historiesState.emit(itemList)
            }
        }
    }

    private suspend fun addHistory(itemList: MutableList<HistoryItem>) {
        val data =
            _historiesState.value.filter { it.type != HistoryType.LOADMORE_HISTORY }.toMutableList()
        data.addAll(itemList)
        _historiesState.emit(data)
    }

    fun addHistoryDetail(history: History?) {
        viewModelScope.launch {
            _historyDetailState.emit(history)
        }
    }

    fun getMarketHistory(timestamp: Long) {
        viewModelScope.launch {
        }
    }
}