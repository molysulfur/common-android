package com.awonar.app.ui.socialtrade.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SocialTradeFilterViewModel @Inject constructor() : ViewModel() {

    val filterItems: StateFlow<MutableList<SocialTradeFilterItem>> =
        flow {
            val list = mutableListOf<SocialTradeFilterItem>()
            list.add(SocialTradeFilterItem.TextListItem(text = "Time Period", meta = "currentMonth"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Trader Status"))
            list.add(SocialTradeFilterItem.SectionItem(text = "Performance"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Return"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Risk score"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Copy AUM"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Number of copiers"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Allocation"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Active Weeks"))
            list.add(SocialTradeFilterItem.SectionItem(text = "Trading"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Profitable Trades"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Number of Trades"))
            list.add(SocialTradeFilterItem.SectionItem(text = "Advance"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Average position size"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Daily drawdown"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Weekly drawdown"))
            list.add(SocialTradeFilterItem.TextListItem(text = "Country"))
            emit(list)
        }.stateIn(viewModelScope, WhileViewSubscribed, mutableListOf())
}