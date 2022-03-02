package com.awonar.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.history.CopiesHistory
import com.awonar.android.model.history.HistoryRequest
import com.awonar.android.model.history.MarketHistory
import com.awonar.android.shared.domain.history.*
import com.awonar.app.domain.history.ConvertHistoryToItemUseCase
import com.awonar.app.ui.history.adapter.HistoryItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HistoryInsideViewModel @Inject constructor(
    private val filterMarketHistoryUseCase: FilterMarketHistoryUseCase,
    private val filterHistoryUseCase: FilterHistoryUseCase,
    private val filterCopyHistoryUseCase: FilterCopyHistoryUseCase,
    private val getCopiesHistoryUseCase: GetCopiesHistoryUseCase,
    private val getAggregateWithCopyUseCase: GetAggregateWithCopyUseCase,
    private val convertHistoryToItemUseCase: ConvertHistoryToItemUseCase,
) : ViewModel() {

    private val _argreationHistroyState = MutableStateFlow<MarketHistory?>(null)
    val argreationHistroyState: StateFlow<MarketHistory?> get() = _argreationHistroyState

    private val _historiesInsideState = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historiesInsideState: StateFlow<List<HistoryItem>> = _historiesInsideState

    private val _argreationCopiesHistroyState = MutableStateFlow<CopiesHistory?>(null)
    val argreationCopiesHistroyState: StateFlow<CopiesHistory?> get() = _argreationCopiesHistroyState

    private var _timeStamp: MutableStateFlow<Long> = MutableStateFlow(0)
    val timeStamp get() = _timeStamp

    fun setTimestamp(time: Long) {
        viewModelScope.launch {
            _timeStamp.value = time
        }
    }

    fun getHistoryInside(symbol: String) {
        viewModelScope.launch {
            filterHistoryUseCase(
                HistoryRequest(
                    timestamp = _timeStamp.value,
                    filter = "manual",
                    symbol = symbol
                )
            ).collect { result ->
                val data = result.successOr(null)
                val itemList =
                    convertHistoryToItemUseCase(
                        data?.history?.toMutableList() ?: mutableListOf()
                    ).successOr(mutableListOf())
                _historiesInsideState.value = itemList
            }
        }
    }

    fun getArgreation(symbol: String) {
        viewModelScope.launch {
            filterMarketHistoryUseCase(
                HistoryRequest(
                    timestamp = _timeStamp.value,
                    filter = "manual",
                    symbol = symbol
                )
            ).collect { result ->
                val data = result.successOr(null)
                _argreationHistroyState.value = data?.markets?.get(0)
            }
        }
    }

    fun getCopiesHistory(username: String) {
        viewModelScope.launch {
            getCopiesHistoryUseCase(
                HistoryRequest(
                    username = username,
                    timestamp = _timeStamp.value
                )
            ).collect { result ->
                val data = result.successOr(null)

                /**
                 * create MarketHistory
                 */
                val marketHistory: MarketHistory? = data?.aggregateCopy?.let {
                    MarketHistory(
                        picture = it.master?.picture,
                        endEquity = it.endEquity,
                        totalFees = it.totalFees,
                        totalInvested = it.totalInvestment,
                        master = it.master,
                        instrumentId = null,
                        symbol = null,
                        totalNetProfit = 0f,
                        totalPositions = 0f
                    )
                }
                _argreationHistroyState.value = marketHistory
                _historiesInsideState.value = copiesHistoryToItem(data?.historyCopy ?: emptyList())
            }
        }
    }

    private fun copiesHistoryToItem(list: List<CopiesHistory>): MutableList<HistoryItem> {
        val itemList = mutableListOf<HistoryItem>()
        list.forEach {
            itemList.add(
                HistoryItem.PositionItem(
                    invested = it.totalInvestment,
                    picture = it.master?.picture,
                    pl = it.totalNetProfit,
                    plPercent = it.totalNetProfit.times(100).div(it.totalInvestment),
                    detail = it.master?.username,
                    transactionType = 0,
                    history = null,
                    master = it.master,
                    positionType = "user2",
                    id = it.copyId,
                    fee = it.totalFees,
                    endValue = it.endEquity
                )
            )
        }
        return itemList
    }

    fun getCopiesHistory(id: String, filter: String, page: Int = 1) {
        viewModelScope.launch {
            filterCopyHistoryUseCase(
                HistoryRequest(
                    timestamp = _timeStamp.value,
                    page = page,
                    filter = filter,
                    copyId = id
                )
            ).collect {
                val data = it.successOr(null)
                val itemList = convertHistoryToItemUseCase(
                    data?.history?.toMutableList() ?: mutableListOf()
                ).successOr(
                    mutableListOf()
                )
                _historiesInsideState.value = itemList

            }
        }
    }

    fun getArgreationWithCopy(copiesId: String) {
        viewModelScope.launch {
            getAggregateWithCopyUseCase(copiesId).collect { result ->
                _argreationCopiesHistroyState.value = result.successOr(null)
            }
        }
    }

}