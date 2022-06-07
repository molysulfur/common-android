package com.awonar.app.ui.portfolio.position

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.portfolio.*
import com.awonar.android.shared.domain.portfolio.*
import com.awonar.app.domain.portfolio.*
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PositionViewModel @Inject constructor(
    private val getPositionPublicBySymbolUseCase: GetPositionPublicBySymbolUseCase,
    private val getPieChartMarketAllocateUseCase: GetPieChartMarketAllocateUseCase,
    private val getPieChartInstrumentAllocateUseCase: GetPieChartInstrumentAllocateUseCase,
    private var getPieChartAllocateUseCase: GetPieChartAllocateUseCase,
    private var getPieChartExposureUseCase: GetPieChartExposureUseCase,
    private val getPieChartInstrumentExposureUseCase: GetPieChartInstrumentExposureUseCase,
    private var convertExposureToPieChartUseCase: ConvertExposureToPieChartUseCase,
    private val convertManualPositionToItemUseCase: ConvertManualPositionToItemUseCase,
    private val convertPositionToCardItemUseCase: ConvertPositionToCardItemUseCase,
    private val convertMarketToItemUseCase: ConvertMarketToItemUseCase,
    private var convertAllocateToPieChartUseCase: ConvertAllocateToPieChartUseCase,
) : ViewModel() {
    private val _chartType = MutableStateFlow("allocate")
    private val _chart = MutableStateFlow("allocate")

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
        val watchChart = combine(
            _chartType,
            _styleTypeState
        ) { chartType, style ->
            Timber.e("$chartType,$style")
            if (style == "chart") {
                when (chartType) {
                    "allocate" -> getAllocate("market")
                    "exposure" -> getExposure(chartType)
                }
            }
        }
        viewModelScope.launch {
            watchChart.collectIndexed { _, value ->

            }
        }
        val watchPortfolio = combine(
            _portfolio,
            _styleTypeState
        ) { portfolio, style ->
            portfolio?.let {
                when (style) {
                    "market" -> convertMarket(portfolio)
                    "manual" -> convertManual(portfolio)
                    "card" -> convertCard(portfolio)
                    else -> {}
                }
            }
            Unit
        }
        viewModelScope.launch {
            watchPortfolio.collectLatest {
            }
        }
    }

    fun updateChartType(it: String = "allocate") {
        _chartType.value = it
    }

    private fun getAllocate(type: String? = null) {
        viewModelScope.launch {
            when (type) {
                "market" -> getPieChartMarketAllocateUseCase(Unit)
                "stocks", "currencies", "crypto" -> getPieChartInstrumentAllocateUseCase(type)
                else -> getPieChartAllocateUseCase(Unit)
            }.collectIndexed { _, value ->
                val data = value.successOr(emptyMap())
                val items = convertAllocateToPieChartUseCase(
                    PieChartRequest(
                        data,
                        type in arrayListOf("stocks", "currencies", "crypto")
                    )
                ).successOr(mutableListOf())
                _positionItems.emit(items.toMutableList())
            }
        }
    }

    private fun getExposure(type: String? = null) {
        viewModelScope.launch {
            when (type) {
                "stocks", "currencies", "crypto" -> getPieChartInstrumentExposureUseCase(type)
                else -> getPieChartExposureUseCase(Unit)
            }.collectLatest {
                val data = it.successOr(emptyMap())
                val items = convertExposureToPieChartUseCase(
                    PieChartRequest(
                        data,
                        type in arrayListOf("stocks", "currencies", "crypto")
                    )
                ).successOr(emptyList())
                _positionItems.emit(items.toMutableList())

            }
        }
    }

    fun convertManual(portfolio: UserPortfolioResponse?) {
        viewModelScope.launch {
            portfolio?.let {
                val positionItemResult =
                    convertManualPositionToItemUseCase(it).successOr(emptyList())
                _positionItems.emit(positionItemResult.toMutableList())
            }

        }
    }

    private fun convertMarket(
        portfolio: UserPortfolioResponse,
    ) {
        viewModelScope.launch {
            val itemLists = convertMarketToItemUseCase(portfolio).successOr(mutableListOf())
            _positionItems.value = itemLists
        }
    }

    fun convertCard(userPortfolio: UserPortfolioResponse) {
        viewModelScope.launch {
            val positionItemResult =
                convertPositionToCardItemUseCase(userPortfolio).successOr(mutableListOf())
            _positionItems.value = positionItemResult
        }
    }

    fun navigateInstrumentInside(position: Int) {
        viewModelScope.launch {
            _navigateActions.send(
                PositionFragmentDirections
                    .actionPositionFragmentToPortFolioInsideInstrumentPortfolioFragment(position)
            )
        }
    }

    fun navigateInstrumentCopier(position: Int) {
        viewModelScope.launch {
            _navigateActions.send(
                PositionFragmentDirections
                    .actionPositionFragmentToPortFolioInsideCopierPortfolioFragment(position)
            )
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