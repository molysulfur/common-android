package com.awonar.app.ui.socialtrade.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.constrant.timePeriods
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SocialTradeFilterViewModel @Inject constructor() : ViewModel() {

    val periodsState: StateFlow<MutableList<SocialTradeFilterItem>> = flow {
        val periods = timePeriods
        val list = mutableListOf<SocialTradeFilterItem>()
        periods.forEach { period ->
            list.add(SocialTradeFilterItem.SelectorListItem(
                text = period.first))
        }
        emit(list)
    }.stateIn(viewModelScope, WhileViewSubscribed, mutableListOf())

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
                meta = "currentMonth"))
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