package com.awonar.app.ui.profile.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.portfolio.HistoryPositionRequest
import com.awonar.android.shared.domain.profile.GetHistoryPositionsUseCase
import com.awonar.app.domain.profile.ConvertHistoryPositionToItemUseCase
import com.awonar.app.ui.history.adapter.HistoryItem
import com.awonar.app.ui.profile.history.adapter.HistoryProfileItem
import com.awonar.app.utils.DateUtils
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HistoryProfileViewModel @Inject constructor(
    private val getHistoryPositionsUseCase: GetHistoryPositionsUseCase,
    private val convertHistoryPositionToItemUseCase: ConvertHistoryPositionToItemUseCase,
) : ViewModel() {

    private val _page = MutableStateFlow(1)
    private val _dateType = MutableStateFlow("30D")

    private val _historiesState = MutableStateFlow<List<HistoryProfileItem>>(emptyList())
    val historiesState: StateFlow<List<HistoryProfileItem>> = _historiesState

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
        viewModelScope.launch {
            getHistoryPositionsUseCase(HistoryPositionRequest(_page.value, timestamp)).collect {
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
}