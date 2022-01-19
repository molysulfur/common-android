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
import javax.inject.Inject

@HiltViewModel
class SocialTradeViewModel @Inject constructor(
    private val getRecommendedUseCase: GetRecommendedUseCase,
    private val getTradersUseCase: GetTradersUseCase,
) : ViewModel() {


    private val traderMostCopies = flow {
        getTradersUseCase(
            TradersRequest(
                period = arrayListOf("1MonthAgo"),
                verified = true,
                maxRisk = 7,
                sort = arrayListOf("-gain", "username"),
                page = 1
            )
        ).collect {
            emit(it.successOr(emptyList()))
        }
    }
    private val lowRisk = flow {
        getTradersUseCase(
            TradersRequest(
                period = arrayListOf("1MonthAgo"),
                verified = true,
                maxRisk = 7,
                sort = arrayListOf("risk", "-gain", "username"),
                page = 1
            )
        ).collect {
            emit(it.successOr(emptyList()))
        }
    }

    private val longTerm = flow {
        getTradersUseCase(
            TradersRequest(
                verified = true,
                maxRisk = 7,
                sort = arrayListOf("-gain", "username"),
                period = arrayListOf("6MonthsAgo"),
                page = 1
            )
        ).collect {
            emit(it.successOr(emptyList()))
        }
    }

    private val shortTerm = flow {
        getTradersUseCase(
            TradersRequest(
                verified = true,
                sort = arrayListOf("-gain", "username"),
                period = arrayListOf("1MonthAgo"),
                maxRisk = 8,
                page = 1
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
            itemList.add(SocialTradeItem.TitleItem("Most Copies"))
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
            itemList.add(SocialTradeItem.TitleItem("Low Risk"))
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
            itemList.add(SocialTradeItem.TitleItem("Long Term"))
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
            itemList.add(SocialTradeItem.TitleItem("Short Term"))
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