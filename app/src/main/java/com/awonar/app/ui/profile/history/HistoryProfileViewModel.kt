package com.awonar.app.ui.profile.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.portfolio.HistoryPositionRequest
import com.awonar.android.shared.domain.profile.GetHistoryPositionsUseCase
import com.awonar.app.domain.profile.ConvertHistoryPositionToItemUseCase
import com.awonar.app.ui.history.adapter.HistoryItem
import com.awonar.app.ui.profile.history.adapter.HistoryProfileItem
import com.awonar.app.utils.DateUtils
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
    private val convertHistoryPositionToItemUseCase: ConvertHistoryPositionToItemUseCase,
) : ViewModel() {

    private val _page = MutableStateFlow(1)
    private val _symbol = MutableStateFlow("")
    private val _dateType = MutableStateFlow("30D")
    val dateType get() = _dateType

    private val _historiesState = MutableStateFlow<List<HistoryProfileItem>>(emptyList())
    val historiesState: StateFlow<List<HistoryProfileItem>> = _historiesState

    private val _navigateAction = Channel<NavDirections>(Channel.CONFLATED)
    val navigateAction: Flow<NavDirections> = _navigateAction.receiveAsFlow()

    private val _from = MutableStateFlow("")
    val from: StateFlow<String> = _from
    private val _to = MutableStateFlow("")
    val to: StateFlow<String> = _to

    fun getHistoryPosition() {
        val prev7Day = Calendar.getInstance()
        _from.value = DateUtils.getDate(prev7Day.timeInMillis)
        val timestamp: Long = when (_dateType.value) {
            "30D" -> {
                prev7Day.add(Calendar.MONTH, -1)
                prev7Day.timeInMillis / 1000
            }
            else -> 0L
        }
        _to.value = DateUtils.getDate(timestamp)
        val request = if (!_symbol.value.isNullOrBlank()) {
            HistoryPositionRequest(_page.value, timestamp, _symbol.value)
        } else {
            HistoryPositionRequest(_page.value, timestamp)
        }
        Timber.e("$request")
        viewModelScope.launch {
            getHistoryPositionsUseCase(request).collect {
                val data = it.successOr(emptyList())
                val itemList =
                    convertHistoryPositionToItemUseCase(it.successOr(emptyList())).successOr(
                        mutableListOf())
                _page.value = if (data.isEmpty()) {
                    0
                } else {
                    itemList.add(HistoryProfileItem.LoadMoreItem(_page.value + 1))
                    _page.value + 1
                }
                _historiesState.value = itemList

            }
        }
    }

    fun openInside(index: Int) {
        viewModelScope.launch {
            val item = _historiesState.value[index]
            Timber.e("$item $index")
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
        _page.value = 0
        _historiesState.value = mutableListOf()
    }
}