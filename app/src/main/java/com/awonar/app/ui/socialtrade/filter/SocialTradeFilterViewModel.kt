package com.awonar.app.ui.socialtrade.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.constrant.returnData
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.ui.socialtrade.filter.adapter.SocialTradeFilterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SocialTradeFilterViewModel @Inject constructor() : ViewModel() {

    private val payload = MutableStateFlow<Map<String, MutableList<String>?>>(emptyMap())

    private val _selectPeriodList = mutableSetOf<String>()
    private val _inputCustom = arrayListOf<String>()
    private var _itemList = mutableListOf<SocialTradeFilterItem>()

    private val _itemListState =
        MutableStateFlow<MutableList<SocialTradeFilterItem>>(mutableListOf())
    val itemListState: StateFlow<MutableList<SocialTradeFilterItem>> get() = _itemListState

    fun updateCustomValue(first: String?, second: String?) {
        if (!first.isNullOrEmpty() && !second.isNullOrEmpty()) {
            _inputCustom.add(first)
            _inputCustom.add(second)
        }
    }

    fun save(key: String) {
        val newMep: HashMap<String, MutableList<String>?> = hashMapOf()
        when (key) {
            "period" -> newMep[key] = _selectPeriodList.toMutableList()
            "status" -> {
                newMep["verified"] =
                    if (_selectPeriodList.contains("verified")) arrayListOf("true") else null
                val selectedPopular = _selectPeriodList.find { it == "popular" }
                newMep["popular"] =
                    if (selectedPopular != null) arrayListOf(selectedPopular) else null
            }
            "return" -> {
                val data = returnData.find { it.first == _selectPeriodList.toMutableList()[0] }
                if (_inputCustom.size > 0) {
                    newMep["return"] = _inputCustom
                } else {
                    data?.let {
                        newMep["return"] = arrayListOf(it.second[0] ?: "", it.second[1] ?: "")
                    }
                }
            }
        }
        payload.value = newMep
    }

    fun setFilterList(key: String, filters: List<Pair<String, String>>) {
        val list = mutableListOf<SocialTradeFilterItem>()
        payload.value[key]?.map {
            _selectPeriodList.add(it)
        } ?: mutableListOf()
        filters.forEach { filter ->
            list.add(SocialTradeFilterItem.MultiSelectorListItem(
                text = filter.first,
                isChecked = _selectPeriodList.contains(filter.first)))
        }
        _itemList = list
        publish()
    }

    fun setFilterList(
        key: String,
        description: String,
        filters: ArrayList<Pair<String, List<String?>>>,
    ) {
        val list = mutableListOf<SocialTradeFilterItem>()
        val data =
            filters.find { it == Pair(payload.value[key]?.get(0), payload.value[key]?.get(1)) }
        data?.let { _selectPeriodList.add(it.first) }
        list.add(SocialTradeFilterItem.DescriptionItem(description))
        filters.forEach { filter ->
            list.add(SocialTradeFilterItem.SingleSelectorListItem(
                text = filter.first,
                isChecked = _selectPeriodList.contains(filter.first)))
        }
        list.add(SocialTradeFilterItem.RangeInputItem())
        _itemList = list
        publish()
    }

    fun toggleMultiple(period: SocialTradeFilterItem.MultiSelectorListItem) {
        val changed = if (period.isChecked) {
            _selectPeriodList.add(period.text)
        } else {
            _selectPeriodList.remove(period.text)
        }

        if (changed) {
            _itemList = _itemList.mapTo(mutableListOf()) { item ->
                if (item is SocialTradeFilterItem.MultiSelectorListItem) {
                    SocialTradeFilterItem.MultiSelectorListItem(
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

    fun toggleSingle(item: SocialTradeFilterItem.SingleSelectorListItem) {
        _selectPeriodList.clear()
        val changed = if (item.isChecked) {
            _selectPeriodList.add(item.text)
        } else {
            _selectPeriodList.remove(item.text)
        }
        if (changed) {
            _itemList = _itemList.mapTo(mutableListOf()) { item ->
                if (item is SocialTradeFilterItem.SingleSelectorListItem) {
                    SocialTradeFilterItem.SingleSelectorListItem(
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
        _itemListState.value = _itemList
    }

    fun clear() {
        _itemList.clear()
        _inputCustom.clear()
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
                meta = payload.value["period"]?.joinToString(",")))
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