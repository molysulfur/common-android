package com.awonar.app.ui.socialtrade.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.constrant.inputData
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

    private val fieldName = mutableMapOf(
        "return" to "minGain,maxGain",
        "risk" to "minRisk,maxRisk",
        "numberCopy" to "minCopiers,maxCopiers",
        "active" to "minActiveWeek,maxActiveWeek",
        "profitable" to "minProfitablePercentage,maxProfitablePercentage",
        "numberTrades" to "minTrades,maxTrades",
        "daily" to "minDailyDrawdown,maxDailyDrawdown",
        "weekly" to "minWeeklyDrawdown,maxWeeklyDrawdown"
    )

    private val _payload = MutableStateFlow<MutableMap<String, String?>>(mutableMapOf())
    val payload get() = _payload

    private val _selectPeriodList = mutableSetOf<String>()
    private var _inputCustom = arrayListOf<String>()
    private var _itemList = mutableListOf<SocialTradeFilterItem>()

    private val _itemListState =
        MutableStateFlow<MutableList<SocialTradeFilterItem>>(mutableListOf())
    val itemListState: StateFlow<MutableList<SocialTradeFilterItem>> get() = _itemListState

    private val _sendRequest = Channel<Map<String, String?>>(Channel.CONFLATED)
    val sendRequest get() = _sendRequest.receiveAsFlow()

    fun getRequest() {
        viewModelScope.launch {
            val request: Map<String, String?> = _payload.value.filter { it.value != null }
            Timber.e("$request")
            _sendRequest.send(request)
        }
    }

    fun updateCustomValue(first: String?, second: String?) {
        if (!first.isNullOrEmpty() && !second.isNullOrEmpty()) {
            _inputCustom = arrayListOf(first, second)
        }
    }

    fun save(key: String) {
        val newMep: MutableMap<String, String?> = _payload.value
        when (key) {
            "status" -> {
                newMep["verified"] =
                    if (_selectPeriodList.contains("verified")) "true" else null
                val selectedPopular = _selectPeriodList.find { it == "popular" }
                newMep["popular"] = selectedPopular
            }
            "return", "risk", "numberCopy", "active", "profitable", "numberTrades", "daily", "weekly" -> {
                val fieldNames = fieldName[key]?.split(",")
                if (_inputCustom.size > 0) {
                    fieldNames?.get(0)?.let {
                        newMep[it] = _inputCustom[0]
                    }
                    fieldNames?.get(1)?.let {
                        newMep[it] = _inputCustom[1]
                    }
                } else {
                    val data =
                        inputData[key]?.find { it.first == _selectPeriodList.toMutableList()[0] }
                    data?.let {
                        fieldNames?.get(0)?.let { key ->
                            newMep[key] = it.second[0]
                        }
                        fieldNames?.get(1)?.let { key ->
                            newMep[key] = it.second[1]
                        }
                    }
                }
            }
            else -> newMep[key] = _selectPeriodList.joinToString(",")
        }
        _payload.value = newMep
        clear()
    }

    fun setFilterList(key: String, filters: List<Pair<String, String>>) {
        val list = mutableListOf<SocialTradeFilterItem>()
        _payload.value[key]?.split(",")?.map {
            _selectPeriodList.add(it)
        } ?: mutableListOf()
        filters.forEach { filter ->
            list.add(SocialTradeFilterItem.MultiSelectorListItem(
                text = filter.first,
                value = filter.second,
                isChecked = _selectPeriodList.contains(filter.second)))
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
        val keys = fieldName[key]
        val data =
            filters.find {
                it == Pair(
                    _payload.value[keys?.get(0) ?: ""]?.get(0),
                    _payload.value[keys?.get(1) ?: ""]?.get(1)
                )
            }
        data?.let { _selectPeriodList.add(it.first) }
        list.add(SocialTradeFilterItem.DescriptionItem(description))
        filters.forEach { filter ->
            list.add(SocialTradeFilterItem.SingleSelectorListItem(
                text = filter.first,
                isChecked = _selectPeriodList.contains(filter.first)))
        }
        list.add(SocialTradeFilterItem.RangeInputItem(
            "Minimum",
            "Maximum"
        ))
        _itemList = list
        publish()
    }

    fun toggleMultiple(period: SocialTradeFilterItem.MultiSelectorListItem) {
        val changed = if (period.isChecked) {
            _selectPeriodList.add(period.value)
        } else {
            _selectPeriodList.remove(period.value)
        }

        if (changed) {
            _itemList = _itemList.mapTo(mutableListOf()) { item ->
                if (item is SocialTradeFilterItem.MultiSelectorListItem) {
                    SocialTradeFilterItem.MultiSelectorListItem(
                        text = item.text,
                        value = item.value,
                        icon = item.icon,
                        iconRes = item.iconRes,
                        isChecked = _selectPeriodList.contains(item.value)
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
                meta = _payload.value["period"]))
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
            emit(list)
        }.stateIn(viewModelScope, WhileViewSubscribed, mutableListOf())
}

