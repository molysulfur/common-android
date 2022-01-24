package com.awonar.app.ui.socialtrade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.socialtrade.Trader
import com.awonar.android.model.socialtrade.TradersRequest
import com.awonar.android.shared.domain.socialtrade.GetRecommendedUseCase
import com.awonar.android.shared.domain.socialtrade.GetTradersUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.ui.socialtrade.adapter.SocialTradeItem
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
    fun onLoad(request: Map<String, String>) {
        viewModelScope.launch {
            getTradersUseCase(
                TradersRequest(
                    filter = request,
                    page = 1,
                    limit = 5
                )
            ).collect {
                Timber.e("${it.successOr(emptyList())}")
            }
        }
    }

    private val traderMostCopies = flow {
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
            emit(it.successOr(emptyList()))
        }
    }

    private val lowRisk = flow {
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
            emit(it.successOr(emptyList()))
        }
    }

    private val longTerm = flow {
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
            emit(it.successOr(emptyList()))
        }
    }

    private val shortTerm = flow {
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
            emit(it.successOr(emptyList()))
        }
    }

    val recommendedState: StateFlow<List<Trader>?> = flow {
        getRecommendedUseCase(Unit).collect {
            emit(it.successOr(emptyList()))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, listOf())

    private val _copiesList =
        combine(
            traderMostCopies,
            lowRisk,
            longTerm,
            shortTerm
        ) { mostCopies, lowRisks, longTerm, shortTerm ->
            val itemList = mutableListOf<SocialTradeItem>()
            itemList.add(SocialTradeItem.TitleItem("Most Copies", "View More"))
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
            itemList.add(SocialTradeItem.TitleItem("Low Risk", "View More"))
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
            itemList.add(SocialTradeItem.TitleItem("Long Term", "View More"))
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
            itemList.add(SocialTradeItem.TitleItem("Short Term", "View More"))
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
    val copiesList get() = _copiesList


}