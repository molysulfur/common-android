package com.awonar.app.ui.socialtrade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.socialtrade.Trader
import com.awonar.android.model.socialtrade.TradersRequest
import com.awonar.android.shared.domain.socialtrade.GetRecommendedUseCase
import com.awonar.android.shared.domain.socialtrade.GetTradersUseCase
import com.awonar.app.ui.socialtrade.adapter.SocialTradeItem
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SocialTradeViewModel @Inject constructor(
    private val getRecommendedUseCase: GetRecommendedUseCase,
    private val getTradersUseCase: GetTradersUseCase,
) : ViewModel() {

    private val _filter: MutableStateFlow<MutableMap<String, String>?> = MutableStateFlow(null)
    val filter get() = _filter
    private var page = 0

    private val traderMostCopies = MutableStateFlow<List<Trader>?>(emptyList())
    private val lowRisk = MutableStateFlow<List<Trader>?>(emptyList())
    private val longTerm = MutableStateFlow<List<Trader>?>(emptyList())
    private val shortTerm = MutableStateFlow<List<Trader>?>(emptyList())

    private val _recommendedState = MutableStateFlow<List<Trader>?>(emptyList())
    val recommendedState: StateFlow<List<Trader>?> = _recommendedState
    private val _copiesList = MutableStateFlow<MutableList<SocialTradeItem>>(mutableListOf())
    val copiesList get() = _copiesList

    init {
        viewModelScope.launch {
            getTradersUseCase(
                TradersRequest(
                    filter = mapOf(
                        "period" to "1MonthAgo",
                        "verified" to "true",
                        "maxRisk" to "7",
                        "sort" to "risk,-gain,username",
                    ),
                    page = 1,
                    limit = 5
                )
            ).collect {
                lowRisk.emit(it.successOr(emptyList()))
            }
        }
        viewModelScope.launch {
            getTradersUseCase(
                TradersRequest(
                    filter = mapOf(
                        "period" to "6MonthsAgo",
                        "verified" to "true",
                        "maxRisk" to "7",
                        "sort" to "-gain,username",
                    ),
                    page = 1,
                    limit = 5
                )
            ).collect {
                longTerm.emit(it.successOr(emptyList()))
            }
        }
        viewModelScope.launch {
            getTradersUseCase(
                TradersRequest(
                    filter = mapOf(
                        "period" to "1MonthAgo",
                        "verified" to "true",
                        "maxRisk" to "7",
                        "sort" to "-gain,username",
                    ),
                    page = 1,
                    limit = 5
                )
            ).collect {
                traderMostCopies.emit(it.successOr(emptyList()))
            }
        }
        viewModelScope.launch {
            getTradersUseCase(
                TradersRequest(
                    filter = mapOf(
                        "period" to "1MonthAgo",
                        "verified" to "true",
                        "maxRisk" to "8",
                        "sort" to "-gain,username",
                    ),
                    page = 1,
                    limit = 5
                )
            ).collect {
                shortTerm.emit(it.successOr(emptyList()))
            }
        }
        getRecomended()
        getTraderWithCategory()
    }

    private fun getRecomended() {
        viewModelScope.launch {
            getRecommendedUseCase(Unit).collect {
                _recommendedState.emit(it.successOr(emptyList()))
            }
        }
    }

    fun filter() {
        viewModelScope.launch {
            getTradersUseCase(
                TradersRequest(
                    filter = _filter.value,
                    page = page
                )
            ).collect { copiers ->
                if (copiers.succeeded) {
                    val list = copiers.successOr(emptyList())
                    val itemList: MutableList<SocialTradeItem> =
                        _copiesList.value.filter { it !is SocialTradeItem.LoadMore }.toMutableList()
                    if (list.isNullOrEmpty()) {
                        page = 0
                    } else {
                        page++
                    }
                    list?.forEach { trader ->
                        itemList.add(
                            SocialTradeItem.CopiesItem(
                                trader.id,
                                trader.image,
                                title = if (trader.displayFullName) {
                                    "${trader.firstName} ${trader.middleName} ${trader.lastName}"
                                } else {
                                    trader.username
                                },
                                trader.username,
                                false,
                                trader.gain,
                                trader.risk
                            )
                        )
                    }
                    if (page > 0) {
                        itemList.add(SocialTradeItem.LoadMore())
                    }
                    _copiesList.value = itemList
                }
            }
        }
    }

    fun filter(request: Map<String, String>) {
        page = 1
        _filter.value = request.toMutableMap()
        _recommendedState.value = emptyList()
        viewModelScope.launch {
            getTradersUseCase(
                TradersRequest(
                    filter = _filter.value,
                    page = page
                )
            ).collect { copiers ->
                val itemList = mutableListOf<SocialTradeItem>()
                copiers.successOr(emptyList())?.forEach { trader ->
                    itemList.add(
                        SocialTradeItem.CopiesItem(
                            trader.id,
                            trader.image,
                            title = if (trader.displayFullName) {
                                "${trader.firstName} ${trader.middleName} ${trader.lastName}"
                            } else {
                                trader.username
                            },
                            trader.username,
                            false,
                            trader.gain,
                            trader.risk
                        )
                    )
                }
                page++
                itemList.add(SocialTradeItem.LoadMore())
                _copiesList.value = itemList
            }
        }
    }

    fun removeFilter(key: String, value: String) {
        val filters = _filter.value?.toMutableMap()
        val filterSplit = filters?.get(key)?.split(",")
        val newValue = filterSplit?.filter { it != value }
        if (newValue?.size ?: 0 > 0) {
            filters?.set(key, newValue?.joinToString(",") ?: "")
        } else {
            filters?.remove(key)
        }
        filters?.toMap()?.let {
            if (it.entries.isEmpty()) {
                _filter.value = null
                getRecomended()
                getTraderWithCategory()
            } else {
                filter(it)
            }
        }
    }

    private fun getTraderWithCategory() {
        val traders: Flow<MutableList<SocialTradeItem>> = combine(
            traderMostCopies,
            lowRisk,
            longTerm,
            shortTerm
        ) { mostCopies, lowRisks, longTerm, shortTerm ->
            val itemList = mutableListOf<SocialTradeItem>()
            itemList.add(SocialTradeItem.TitleItem("mostCopies", "Most Copies", "View More"))
            mostCopies?.forEach { trader ->
                itemList.add(
                    SocialTradeItem.CopiesItem(
                        trader.id,
                        trader.image,
                        title = if (trader.displayFullName) {
                            "${trader.firstName} ${trader.middleName} ${trader.lastName}"
                        } else {
                            trader.username
                        },
                        trader.username,
                        false,
                        trader.gain,
                        trader.risk
                    )
                )
            }
            itemList.add(SocialTradeItem.TitleItem("lowRisk", "Low Risk", "View More"))
            lowRisks?.forEach { trader ->
                itemList.add(
                    SocialTradeItem.CopiesItem(
                        trader.id,
                        trader.image,
                        title = if (trader.displayFullName) {
                            "${trader.firstName} ${trader.middleName} ${trader.lastName}"
                        } else {
                            trader.username
                        },
                        trader.username,
                        false,
                        trader.gain,
                        trader.risk
                    )
                )
            }
            itemList.add(SocialTradeItem.TitleItem("longTerm", "Long Term", "View More"))
            longTerm?.forEach { trader ->
                itemList.add(
                    SocialTradeItem.CopiesItem(
                        trader.id,
                        trader.image,
                        title = if (trader.displayFullName) {
                            "${trader.firstName} ${trader.middleName} ${trader.lastName}"
                        } else {
                            trader.username
                        },
                        trader.username,
                        false,
                        trader.gain,
                        trader.risk
                    )
                )
            }
            itemList.add(SocialTradeItem.TitleItem("shortTerm", "Short Term", "View More"))
            shortTerm?.forEach { trader ->
                itemList.add(
                    SocialTradeItem.CopiesItem(
                        trader.id,
                        trader.image,
                        title = if (trader.displayFullName) {
                            "${trader.firstName} ${trader.middleName} ${trader.lastName}"
                        } else {
                            trader.username
                        },
                        trader.username,
                        false,
                        trader.gain,
                        trader.risk
                    )
                )
            }
            itemList
        }
        viewModelScope.launch {
            traders.collect {
                _copiesList.emit(it)
            }
        }
    }
}