package com.awonar.app.ui.socialtrade.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.socialtrade.TradersRequest
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialTradeFilterViewModel @Inject constructor() : ViewModel() {

    private val payload = MutableStateFlow(TradersRequest())

    private val _selectedTimePeriod = MutableStateFlow<List<String>>(mutableListOf())
    val selectedTimePeriod get() = _selectedTimePeriod

    private val _selectPeriodList = mutableSetOf<String>()
    private var _itemList = mutableListOf<SocialTradeFilterItem>()

    private val _periodsState =
        MutableStateFlow<MutableList<SocialTradeFilterItem>>(mutableListOf())
    val periodsState: StateFlow<MutableList<SocialTradeFilterItem>> = _periodsState

    fun save(key: String) {
        payload.value.copy().apply {
            when (key) {
                "period" -> period = _selectPeriodList.toList()
            }
        }
    }

    fun setFilterList(filters: List<Pair<String, String>>) {
        val list = mutableListOf<SocialTradeFilterItem>()
        payload.value.period?.map {
            _selectPeriodList.add(it)
        } ?: mutableListOf()
        filters.forEach { filter ->
            list.add(SocialTradeFilterItem.SelectorListItem(
                text = filter.first,
                isChecked = _selectPeriodList.contains(filter.first)))
        }
        _itemList = list
        publish()
    }

    fun toggleTimePeriod(period: SocialTradeFilterItem.SelectorListItem) {
        val changed = if (period.isChecked) {
            _selectPeriodList.add(period.text)
        } else {
            _selectPeriodList.remove(period.text)
        }

        if (changed) {
            _itemList = _itemList.mapTo(mutableListOf()) { item ->
                if (item is SocialTradeFilterItem.SelectorListItem) {
                    SocialTradeFilterItem.SelectorListItem(
                        text = item.text,
                        icon = item.icon,
                        iconRes = item.iconRes,
                        isChecked = _selectPeriodList.contains(item.text)
                    )
                } else {
                    item
                }
            }
            publish()
        }
    }

    private fun publish() {
        _periodsState.value = _itemList
    }

    fun clear() {
        _itemList.clear()
        _selectPeriodList.clear()
    }


    private val _navigateAction = Channel<NavDirections>(Channel.CONFLATED)
    val navigateAction get() = _navigateAction.receiveAsFlow()

    fun navigate(directions: NavDirections) {
        viewModelScope.launch {
            _navigateAction.send(directions)
        }
    }

    val filterItems: StateFlow<MutableList<SocialTradeFilterItem>> =
        flow {
            val list = mutableListOf<SocialTradeFilterItem>()
            list.add(SocialTradeFilterItem.TextListItem(key = "period",
                text = "Time Period",
                meta = payload.value.period?.joinToString(",")))
            list.add(SocialTradeFilterItem.TextListItem(key = "status", text = "Trader Status"))
            list.add(SocialTradeFilterItem.SectionItem(text = "Performance"))
            list.add(SocialTradeFilterItem.TextListItem(key = "return", text = "Return"))
            list.add(SocialTradeFilterItem.TextListItem(key = "risk", text = "Risk score"))
            list.add(SocialTradeFilterItem.TextListItem(key = "numberCopy",
                text = "Number of copiers"))
            list.add(SocialTradeFilterItem.TextListItem(key = "allocation", text = "Allocation"))
            list.add(SocialTradeFilterItem.TextListItem(key = "active", text = "Active Weeks"))
            list.add(SocialTradeFilterItem.SectionItem(text = "Trading"))
            list.add(SocialTradeFilterItem.TextListItem(key = "profitable",
                text = "Profitable Trades"))
            list.add(SocialTradeFilterItem.TextListItem(key = "numberTrades",
                text = "Number of Trades"))
            list.add(SocialTradeFilterItem.SectionItem(text = "Advance"))
            list.add(SocialTradeFilterItem.TextListItem(key = "daily", text = "Daily drawdown"))
            list.add(SocialTradeFilterItem.TextListItem(key = "weekly", text = "Weekly drawdown"))
            list.add(SocialTradeFilterItem.TextListItem(key = "country", text = "Country"))
            emit(list)
        }.stateIn(viewModelScope, WhileViewSubscribed, mutableListOf())
}