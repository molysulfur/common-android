package com.awonar.app.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.awonar.android.model.market.Quote
import com.awonar.android.model.portfolio.*
import com.awonar.android.shared.domain.market.GetConversionByInstrumentUseCase
import com.awonar.android.shared.domain.portfolio.*
import com.awonar.android.shared.utils.PortfolioUtil
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.ui.portfolio.adapter.PortfolioItem
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.awonar.app.domain.portfolio.*
import com.awonar.app.ui.profile.user.PublicPortfolioFragmentDirections
import kotlinx.coroutines.channels.Channel

@HiltViewModel
class PortFolioViewModel @Inject constructor(
    private val getMyPortFolioUseCase: GetMyPortFolioUseCase,
    private var getPositionMarketUseCase: GetPositionMarketUseCase,
    private val getPositionManualUseCase: GetPositionManualUseCase,
    private val getPositionUseCase: GetPositionUseCase,
    private val getConversionByInstrumentUseCase: GetConversionByInstrumentUseCase,
    private val getPendingOrdersUseCase: GetPendingOrdersUseCase,
    private var convertPositionToItemUseCase: ConvertPositionToItemUseCase,
    private var convertPositionToCardItemUseCase: ConvertPositionToCardItemUseCase,
    private var convertCopierToCardItemUseCase: ConvertCopierToCardItemUseCase,
    private var convertOrderPositionToItemUseCase: ConvertOrderPositionToItemUseCase,
    private var getPieChartExposureUseCase: GetPieChartExposureUseCase,
    private var getPieChartAllocateUseCase: GetPieChartAllocateUseCase,
    private var convertExposureToPieChartUseCase: ConvertExposureToPieChartUseCase,
    private var convertAllocateToPieChartUseCase: ConvertAllocateToPieChartUseCase,
    private val getPieChartMarketAllocateUseCase: GetPieChartMarketAllocateUseCase,
    private val getPieChartInstrumentAllocateUseCase: GetPieChartInstrumentAllocateUseCase,
    private val getPieChartInstrumentExposureUseCase: GetPieChartInstrumentExposureUseCase,
    private val convertManualPositionToItemUseCase: ConvertManualPositionToItemUseCase,
) : ViewModel() {

    private val _portfolioType = MutableStateFlow("market")

    private val _profitState = MutableStateFlow(0f)
    val profitState: StateFlow<Float> get() = _profitState
    private val _equityState = MutableStateFlow(0f)
    val equityState: StateFlow<Float> get() = _equityState
    private val _positionList: MutableStateFlow<MutableList<PortfolioItem>> =
        MutableStateFlow(mutableListOf(PortfolioItem.EmptyItem()))
    val positionList: StateFlow<MutableList<PortfolioItem>> get() = _positionList
    private val _historyState: MutableStateFlow<MutableList<PortfolioItem>> =
        MutableStateFlow(mutableListOf(PortfolioItem.EmptyItem()))
    val historyState: StateFlow<MutableList<PortfolioItem>> get() = _historyState
    private val _positionState = MutableStateFlow<UserPortfolioResponse?>(null)
    val positionState: StateFlow<UserPortfolioResponse?> get() = _positionState

    private val _navigateActions = Channel<NavDirections>(Channel.CONFLATED)
    val navigateActions get() = _navigateActions.receiveAsFlow()
    val portfolioState: StateFlow<Portfolio?> = flow {
        getMyPortFolioUseCase(true).collect {
            val data = it.successOr(null)
            this.emit(data)
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    fun navigate(index: Int) {
        viewModelScope.launch {
            _navigateActions.send(PublicPortfolioFragmentDirections.publicPortfolioFragmentToInsidePositionPortfolioFragment(
                index))
        }
    }

    fun getManual(username: String? = null) {
        viewModelScope.launch {
            getPositionManualUseCase(username).collect {
                val data = it.successOr(null)
                val itemList = convertManualPositionToItemUseCase(data).successOr(mutableListOf())
                _positionList.emit(itemList)
            }
        }
    }

    fun getPosition() {
        viewModelScope.launch {
            getPositionMarketUseCase(Unit).collect {
                val data = it.successOr(null)
                _positionState.value = data
            }
        }
    }


    fun togglePortfolio(type: String) {
        viewModelScope.launch {
            _portfolioType.emit(type)
        }
    }

    fun getPosition(instrumentId: Int) {
        viewModelScope.launch {
            getPositionUseCase(instrumentId).collect {
                val position = it.successOr(emptyList())
                _positionList.emit(
                    convertPositionToItemUseCase(position).successOr(emptyList()).toMutableList()
                )
//                _positionState.emit(position)
            }
        }
    }

    fun getCardPosition() {
        viewModelScope.launch {
            getPositionMarketUseCase(Unit).collect { result ->
                val itemList = mutableListOf<PortfolioItem>()
                val positionResult = convertPositionToCardItemUseCase(
                    result.data?.positions ?: emptyList()
                ).successOr(
                    emptyList()
                )
                val copierResult =
                    convertCopierToCardItemUseCase(result.data?.copies ?: emptyList()).successOr(
                        emptyList()
                    )
                itemList.addAll(positionResult)
                itemList.addAll(copierResult)
                _positionList.emit(itemList)
            }
        }
    }

    fun getOrdersPosition() {
        viewModelScope.launch {
            getPendingOrdersUseCase(Unit).collect {
                val data = it.successOr(mutableListOf())
                _positionList.emit(
                    convertOrderPositionToItemUseCase(data).successOr(listOf(PortfolioItem.EmptyItem()))
                        .toMutableList()
                )
            }
        }
    }

    fun getExposure(type: String? = null) {
        viewModelScope.launch {
            when (type) {
                "stocks", "currencies", "crypto" -> getPieChartInstrumentExposureUseCase(type)
                else -> getPieChartExposureUseCase(Unit)
            }.collect {
                val data = it.successOr(emptyMap())
                val items = convertExposureToPieChartUseCase(
                    PieChartRequest(
                        data,
                        type in arrayListOf("stocks", "currencies", "crypto")
                    )
                ).successOr(emptyList())
                _positionList.emit(items.toMutableList())
            }
        }
    }

    fun getAllocate(type: String? = null) {
        viewModelScope.launch {
            when (type) {
                "market" -> getPieChartMarketAllocateUseCase(Unit)
                "stocks", "currencies", "crypto" -> getPieChartInstrumentAllocateUseCase(type)
                else -> getPieChartAllocateUseCase(Unit)
            }.collect {
                val data = it.successOr(emptyMap())
                val items = convertAllocateToPieChartUseCase(
                    PieChartRequest(
                        data,
                        type in arrayListOf("stocks", "currencies", "crypto")
                    )
                ).successOr(emptyList())
                _positionList.emit(items.toMutableList())
            }
        }
    }

    fun sumTotalProfitAndEquity(quotes: MutableMap<Int, Quote>) {
        viewModelScope.launch {
            val portfolio = portfolioState.value
            if (portfolio != null) {
                var plSymbol = 0f
                var plCopy = 0f
                _positionState.value?.positions?.forEach { position ->
                    quotes[position.instrument?.id]?.let { quote ->
                        val current = PortfolioUtil.getCurrent(position.isBuy, quote)
                        plSymbol += PortfolioUtil.getProfitOrLoss(
                            current,
                            position.openRate,
                            position.units,
                            getConversionByInstrumentUseCase(position.instrument?.id
                                ?: 0).successOr(0f),
                            position.isBuy)
                    }
                }
                _positionState.value?.copies?.forEach { copier ->
                    copier.positions?.forEach { position ->
                        quotes[position.instrument?.id]?.let { quote ->
                            val current = PortfolioUtil.getCurrent(position.isBuy, quote)
                            plCopy += PortfolioUtil.getProfitOrLoss(
                                current,
                                position.openRate,
                                position.units,
                                getConversionByInstrumentUseCase(position.instrument?.id
                                    ?: 0).successOr(
                                    0f),
                                position.isBuy)
                        }
                    }
                }
                _profitState.value = plSymbol.plus(plCopy)
                _equityState.value =
                    portfolio.available.plus(portfolio.totalAllocated).plus(plSymbol.plus(plCopy))
            }
        }
    }

    fun getPositionIndex(currentIndex: Int): Position? {
        if (currentIndex >= 0 && currentIndex < positionList.value.size) {
            val item = positionList.value[currentIndex]
            if (currentIndex > positionList.value.size || currentIndex < positionList.value.size) {
                if (item is PortfolioItem.InstrumentPortfolioItem) {
                    return item.position
                }
            }
        }
        return null
    }

}