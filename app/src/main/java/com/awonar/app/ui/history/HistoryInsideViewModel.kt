package com.awonar.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.history.HistoryRequest
import com.awonar.android.model.history.MarketHistory
import com.awonar.android.shared.domain.history.FilterHistoryUseCase
import com.awonar.android.shared.domain.history.FilterMarketHistoryUseCase
import com.awonar.app.domain.history.ConvertHistoryToItemUseCase
import com.awonar.app.ui.history.adapter.HistoryItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryInsideViewModel @Inject constructor(
    private val filterMarketHistoryUseCase: FilterMarketHistoryUseCase,
    private val filterHistoryUseCase: FilterHistoryUseCase,
    private val convertHistoryToItemUseCase: ConvertHistoryToItemUseCase
) : ViewModel() {

    private val _argreationHistroyState = MutableStateFlow<MarketHistory?>(null)
    val argreationHistroyState: StateFlow<MarketHistory?> get() = _argreationHistroyState

    private val _historiesInsideState = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historiesInsideState: StateFlow<List<HistoryItem>> = _historiesInsideState

    fun getHistoryInside(symbol: String, timeStamp: Long) {
        viewModelScope.launch {
            filterHistoryUseCase(
                HistoryRequest(
                    timestamp = timeStamp,
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

    fun getArgreation(symbol: String, timeStamp: Long) {
        viewModelScope.launch {
            filterMarketHistoryUseCase(
                HistoryRequest(
                    timestamp = timeStamp,
                    filter = "manual",
                    symbol = symbol
                )
            ).collect { result ->
                val data = result.successOr(null)
                _argreationHistroyState.value = data?.markets?.get(0)
            }
        }
    }
}