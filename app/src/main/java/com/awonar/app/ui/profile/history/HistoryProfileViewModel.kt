package com.awonar.app.ui.profile.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.portfolio.HistoryPositionRequest
import com.awonar.android.model.portfolio.PublicHistoryAggregate
import com.awonar.android.shared.domain.profile.GetHistoryAggregateUseCase
import com.awonar.android.shared.domain.profile.GetHistoryPositionsUseCase
import com.awonar.app.domain.profile.ConvertHistoryPositionToItemUseCase
import com.awonar.app.ui.history.adapter.HistoryItem
import com.awonar.app.ui.profile.history.adapter.HistoryProfileItem
import com.awonar.app.utils.DateUtils
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryProfileViewModel @Inject constructor(
    private val getHistoryPositionsUseCase: GetHistoryPositionsUseCase,
    private val getHistoryAggregateUseCase: GetHistoryAggregateUseCase,
    private val convertHistoryPositionToItemUseCase: ConvertHistoryPositionToItemUseCase,
) : ViewModel() {

    private val _page = MutableStateFlow(1)
    private val _symbol = MutableStateFlow("")
    private val _dateType = MutableStateFlow("30D")
    val dateType get() = _dateType

    private val _historiesState = MutableStateFlow<MutableList<HistoryProfileItem>>(mutableListOf())
    val historiesState: StateFlow<MutableList<HistoryProfileItem>> = _historiesState
    private val _argregateState = MutableStateFlow<PublicHistoryAggregate?>(null)
    val argregateState: StateFlow<PublicHistoryAggregate?> = _argregateState

    private val _navigateAction = Channel<NavDirections>(Channel.CONFLATED)
    val navigateAction: Flow<NavDirections> = _navigateAction.receiveAsFlow()

    private val _from = MutableStateFlow("")
    val from: StateFlow<String> = _from
    private val _to = MutableStateFlow("")
    val to: StateFlow<String> = _to

    fun getHistoryAggregate() {
        viewModelScope.launch {
            val prev7Day = Calendar.getInstance()
            val timestamp: Long = when (_dateType.value) {
                "7D" -> {
                    prev7Day.add(Calendar.DAY_OF_MONTH, -7)
                    prev7Day.timeInMillis / 1000
                }
                "30D" -> {
                    prev7Day.add(Calendar.MONTH, -1)
                    prev7Day.timeInMillis / 1000
                }
                "3M" -> {
                    prev7Day.add(Calendar.MONTH, -3)
                    prev7Day.timeInMillis / 1000
                }
                "6M" -> {
                    prev7Day.add(Calendar.MONTH, -6)
                    prev7Day.timeInMillis / 1000
                }
                "1Y" -> {
                    prev7Day.add(Calendar.YEAR, -3)
                    prev7Day.timeInMillis / 1000
                }
                "Today" -> {
                    prev7Day.add(Calendar.DAY_OF_MONTH, -1)
                    prev7Day.timeInMillis / 1000
                }
                else -> 0L
            }
            getHistoryAggregateUseCase(HistoryPositionRequest(time = timestamp,
                symbol = _symbol.value)).collect {
                _argregateState.value = it.successOr(null)
            }
        }
    }

    fun getHistoryPosition() {
        val prev7Day = Calendar.getInstance()
        _from.value = DateUtils.getDate(prev7Day.timeInMillis)
        val timestamp: Long = when (_dateType.value) {
            "7D" -> {
                prev7Day.add(Calendar.DAY_OF_MONTH, -7)
                prev7Day.timeInMillis / 1000
            }
            "30D" -> {
                prev7Day.add(Calendar.MONTH, -1)
                prev7Day.timeInMillis / 1000
            }
            "3M" -> {
                prev7Day.add(Calendar.MONTH, -3)
                prev7Day.timeInMillis / 1000
            }
            "6M" -> {
                prev7Day.add(Calendar.MONTH, -6)
                prev7Day.timeInMillis / 1000
            }
            "1Y" -> {
                prev7Day.add(Calendar.YEAR, -3)
                prev7Day.timeInMillis / 1000
            }
            "Today" -> {
                prev7Day.add(Calendar.DAY_OF_MONTH, -1)
                prev7Day.timeInMillis / 1000
            }
            else -> 0L
        }
        _to.value = DateUtils.getDate(timestamp * 1000)
        val request = if (!_symbol.value.isNullOrBlank()) {
            HistoryPositionRequest(_page.value, timestamp, _symbol.value)
        } else {
            HistoryPositionRequest(_page.value, timestamp)
        }
        viewModelScope.launch {
            if (_page.value > 0) {
                getHistoryPositionsUseCase(request).collect {
                    if (it.succeeded) {
                        val data = it.data ?: emptyList()
                        val itemList =
                            convertHistoryPositionToItemUseCase(data).successOr(
                                mutableListOf())
                        _page.value = if (data.isEmpty()) {
                            0
                        } else {
                            itemList.add(HistoryProfileItem.LoadMoreItem(_page.value + 1))
                            _page.value + 1
                        }
                        val newList =
                            _historiesState.value.filterIsInstance<HistoryProfileItem.PositionItem>()
                                .toMutableList<HistoryProfileItem>()
                        newList.addAll(itemList)
                        _historiesState.value = newList
                    }
                }
            }
        }
    }

    fun openInside(index: Int) {
        viewModelScope.launch {
            val item = _historiesState.value[index]
            if (item is HistoryProfileItem.PositionItem) {
                _navigateAction.send(HistoryPositionFragmentDirections.historyPositionFragmentToInsideHistoryPositionFragment(
                    item.symbol ?: ""))
            }
        }
    }

    fun setSymbol(symbol: String) {
        _symbol.value = symbol
    }

    fun clear() {
        _page.value = 1
        _argregateState.value = null
        _historiesState.value = mutableListOf()
    }

    fun changeDateType(key: String?) {
        key?.let {
            _dateType.value = key
        }
    }
}