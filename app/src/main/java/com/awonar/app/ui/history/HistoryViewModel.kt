package com.awonar.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.history.Aggregate
import com.awonar.android.model.history.History
import com.awonar.android.model.history.HistoryRequest
import com.awonar.android.shared.domain.history.GetAggregateUseCase
import com.awonar.android.shared.domain.history.GetCashFlowHistoryUseCase
import com.awonar.android.shared.domain.history.GetHistoryUseCase
import com.awonar.android.shared.domain.history.GetMarketHistoryUseCase
import com.awonar.app.domain.history.ConvertCashFlowToItemUseCase
import com.awonar.app.domain.history.ConvertHistoryToItemUseCase
import com.awonar.app.ui.history.adapter.HistoryItem
import com.awonar.app.ui.history.adapter.HistoryType
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAggregateUseCase: GetAggregateUseCase,
    private val getHistoryUseCase: GetHistoryUseCase,
    private val getMarketHistoryUseCase: GetMarketHistoryUseCase,
    private val convertHistoryToItemUseCase: ConvertHistoryToItemUseCase,
    private val convertCashFlowToItemUseCase: ConvertCashFlowToItemUseCase,
    private val getCashFlowHistoryUseCase: GetCashFlowHistoryUseCase,

    ) : ViewModel() {

    private val _aggregateState = MutableStateFlow<Aggregate?>(null)
    val aggregateState: StateFlow<Aggregate?> = _aggregateState

    private val _historiesState = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historiesState: StateFlow<List<HistoryItem>> = _historiesState

    private val _historyDetailState = MutableStateFlow<History?>(null)
    val historyDetailState: StateFlow<History?> = _historyDetailState

    private var _timeStamp: MutableStateFlow<Long>
    var timeStamp: StateFlow<Long>

    init {
        val prev7Day = Calendar.getInstance()
        prev7Day.add(Calendar.DATE, -7)
        _timeStamp = MutableStateFlow(prev7Day.timeInMillis / 1000)
        timeStamp = _timeStamp
        viewModelScope.launch {
            getAggregate(_timeStamp.value)
        }

        getHistory(timestamp = _timeStamp.value)
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
            getHistoryUseCase(HistoryRequest(_timeStamp.value, page)).collect { result ->
                val data = result.successOr(null)
                val itemList: MutableList<HistoryItem> =
                    convertHistoryToItemUseCase(
                        data?.history?.toMutableList() ?: mutableListOf()
                    ).successOr(mutableListOf())
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
            _timeStamp.emit(timestamp)
            getHistoryUseCase(HistoryRequest(_timeStamp.value)).collect { result ->
                val data = result.successOr(null)
                val itemList = convertHistoryToItemUseCase(
                    data?.history?.toMutableList() ?: mutableListOf()
                ).successOr(mutableListOf())
                val page = data?.page ?: 0
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
            getHistoryUseCase(HistoryRequest(_timeStamp.value)).collect { result ->
                val data = result.successOr(null)
                val itemList = convertHistoryToItemUseCase(
                    data?.history?.toMutableList() ?: mutableListOf()
                ).successOr(mutableListOf())
                val page = data?.page ?: 0
                if (page > 0) {
                    itemList.add(HistoryItem.LoadMoreItem(page))
                }
                _historiesState.emit(itemList)
            }
            _historyDetailState.emit(history)
        }
    }

    fun getMarketHistory(timestamp: Long) {
        viewModelScope.launch {
            _historiesState.emit(emptyList())
            _timeStamp.emit(timestamp)
            getMarketHistoryUseCase(HistoryRequest(_timeStamp.value)).collect { result ->
                val data = result.successOr(null)
                val items: List<HistoryItem>? = data?.markets?.map {
                    val picture = it.master?.picture ?: it.picture
                    HistoryItem.PositionItem(
                        picture = picture,
                        invested = it.totalInvested,
                        pl = it.totalNetProfit,
                        plPercent = it.totalNetProfit.times(100).div(it.totalInvested),
                        detail = (it.symbol ?: it.master?.username).toString(),
                        transactionType = 0,
                        positionType = "market"
                    )
                }
                _historiesState.emit(items?.toMutableList() ?: mutableListOf())
            }
        }
    }

    fun getCashflow() {
        viewModelScope.launch {
            val prev7Day = Calendar.getInstance()
            prev7Day.add(Calendar.DATE, -7)
            getCashFlowHistoryUseCase(prev7Day.timeInMillis / 1000).collect {
                val data = it.successOr(listOf())
                val itemList =
                    convertCashFlowToItemUseCase(data ?: listOf()).successOr(mutableListOf())
                _historiesState.value = itemList
            }
        }
    }
}