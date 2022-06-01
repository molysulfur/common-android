package com.awonar.app.ui.portfolio.position

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.*
import com.awonar.android.shared.domain.portfolio.GetPositionPublicBySymbolUseCase
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.app.domain.portfolio.ConvertCopierToItemUseCase
import com.awonar.app.domain.portfolio.ConvertMarketToItemUseCase
import com.awonar.app.domain.portfolio.ConvertPositionToCardItemUseCase
import com.awonar.app.domain.portfolio.ConvertPositionToItemUseCase
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.awonar.app.ui.portfolio.inside.PortFolioInsideCopierFragmentDirections
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PositionViewModel @Inject constructor(
    private val getPositionPublicBySymbolUseCase: GetPositionPublicBySymbolUseCase,
    private val convertPositionToItemUseCase: ConvertPositionToItemUseCase,
    private val convertPositionToCardItemUseCase: ConvertPositionToCardItemUseCase,
    private val convertMarketToItemUseCase: ConvertMarketToItemUseCase,
    private var convertCopierToItemUseCase: ConvertCopierToItemUseCase,
) : ViewModel() {
    private val _portfolio = MutableStateFlow<UserPortfolioResponse?>(null)
    private val _styleTypeState = MutableStateFlow("market")
    val styleTypeState get() = _styleTypeState

    private val _navigateActions = Channel<NavDirections>(Channel.CONFLATED)
    val navigateActions get() = _navigateActions.receiveAsFlow()

    private val _publicPosition: MutableStateFlow<PublicPosition?> = MutableStateFlow(null)
    val publicPosition get() = _publicPosition

    private val _positionItems: MutableStateFlow<MutableList<PortfolioItem>> =
        MutableStateFlow(mutableListOf(PortfolioItem.EmptyItem()))
    val positionItems: StateFlow<MutableList<PortfolioItem>> get() = _positionItems


    init {
        val steaming = combine(
            _portfolio,
            styleTypeState
        ) { portfolio, style ->
            portfolio?.let {
                when (style) {
                    "market" -> convertMarket(
                        portfolio
                    )
                    "manual" -> mutableListOf<PortfolioItem>()
                    "chart" -> mutableListOf<PortfolioItem>()
                    "card" -> mutableListOf<PortfolioItem>()
                    else -> mutableListOf<PortfolioItem>()
                }
            }
            Unit
        }
        viewModelScope.launch {
            steaming.collectLatest {
            }
        }
    }

    fun convertManual(it: List<Position>) {
        viewModelScope.launch {
            val positionItemResult = convertPositionToItemUseCase(it).successOr(emptyList())
            _positionItems.emit(positionItemResult.toMutableList())
        }
    }

    private fun convertMarket(
        portfolio: UserPortfolioResponse,
    ) {
        viewModelScope.launch {
            val itemLists = convertMarketToItemUseCase(
                ConvertMarketRequest(
                    portfolio
                )
            ).successOr(mutableListOf())
            _positionItems.value = itemLists
        }
    }

    fun navigateInstrumentInside(index: Int, type: String) {
        viewModelScope.launch {
            when (type) {
                "instrument" -> _navigateActions.send(
                    PositionFragmentDirections.actionPositionFragmentToPortFolioInsideInstrumentPortfolioFragment(
                        index
                    )
                )
                "copies" -> _navigateActions.send(
                    PositionFragmentDirections.actionPositionFragmentToPortFolioInsideCopierPortfolioFragment(
                        index
                    )
                )
                "copies_instrument" -> _navigateActions.send(
                    PortFolioInsideCopierFragmentDirections.actionPortFolioInsideCopierFragmentToPortFolioInsideInstrumentCopierFragment(
                        index
                    )
                )
            }

        }
    }

    fun convertCard(positions: List<Position>) {
        viewModelScope.launch {
            val positionItemResult =
                convertPositionToCardItemUseCase(positions).successOr(emptyList())
            _positionItems.emit(positionItemResult.toMutableList())
        }
    }

    fun getInsidePublic(username: String?, symbol: String?) {
        viewModelScope.launch {
            if (username != null && symbol != null) {
                getPositionPublicBySymbolUseCase(
                    PublicPositionRequest(username, symbol)
                ).collectLatest {
                    _publicPosition.emit(it.successOr(null))
                }
            }
        }
    }

    fun getPositionListItems(portfolio: UserPortfolioResponse?) {
        _portfolio.value = portfolio
    }

    fun toggleStyle() {
        val newStyle = when (_styleTypeState.value) {
            "market" -> "manual"
            "manual" -> "chart"
            "chart" -> "card"
            "card" -> "market"
            else -> "market"
        }
        _styleTypeState.value = newStyle
    }
}