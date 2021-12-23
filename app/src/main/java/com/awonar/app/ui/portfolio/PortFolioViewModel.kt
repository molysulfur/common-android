package com.awonar.app.ui.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.portfolio.*
import com.awonar.android.shared.domain.portfolio.*
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.ui.portfolio.adapter.OrderPortfolioItem
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.awonar.app.domain.portfolio.*
import timber.log.Timber

@HiltViewModel
class PortFolioViewModel @Inject constructor(
    private val getMyPortFolioUseCase: GetMyPortFolioUseCase,
    private var getPositionMarketUseCase: GetPositionMarketUseCase,
    private val getPositionUseCase: GetPositionUseCase,
    private val getCopierUseCase: GetCopierUseCase,
    private val getPendingOrdersUseCase: GetPendingOrdersUseCase,
    private var convertPositionToItemUseCase: ConvertPositionToItemUseCase,
    private var convertGroupPositionToItemUseCase: ConvertGroupPositionToItemUseCase,
    private var convertPositionWithCopierUseCase: ConvertPositionWithCopierUseCase,
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
) : ViewModel() {

    private val _portfolioType = MutableStateFlow("market")
    val portfolioType: StateFlow<String> get() = _portfolioType

    private val _navigateInsideInstrumentPortfolio =
        Channel<Pair<String, String>>(capacity = Channel.CONFLATED)
    val navigateInsideInstrumentPortfolio: Flow<Pair<String, String>> =
        _navigateInsideInstrumentPortfolio.receiveAsFlow()

    private val _subscricbeQuote = Channel<List<Int>>(capacity = Channel.CONFLATED)
    val subscricbeQuote: Flow<List<Int>> = _subscricbeQuote.receiveAsFlow()

    val portfolioState: StateFlow<Portfolio?> = flow {
        getMyPortFolioUseCase(true).collect {
            this.emit(it.successOr(null))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    private val _positionOrderList: MutableStateFlow<MutableList<OrderPortfolioItem>> =
        MutableStateFlow(mutableListOf(OrderPortfolioItem.EmptyItem()))
    val positionOrderList: StateFlow<MutableList<OrderPortfolioItem>> get() = _positionOrderList

    private val _positionState = MutableStateFlow<UserPortfolioResponse?>(null)
    val positionState: StateFlow<UserPortfolioResponse?> get() = _positionState

    private val _copierState = MutableStateFlow<Copier?>(null)
    val copierState: StateFlow<Copier?> get() = _copierState

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

    fun navigateInsidePortfolio(it: String, type: String) {
        viewModelScope.launch {
            _navigateInsideInstrumentPortfolio.send(Pair(it, type))
        }
    }

    fun getPosition(instrumentId: Int) {
        viewModelScope.launch {
            getPositionUseCase(instrumentId).collect {
                val position = it.successOr(emptyList())
                _positionOrderList.emit(
                    convertPositionToItemUseCase(position).successOr(emptyList()).toMutableList()
                )
//                _positionState.emit(position)
            }
        }
    }

    fun getPosition(copyId: String, id: Int) {
        viewModelScope.launch {
            getCopierUseCase(copyId).collect { result ->
                if (result is Result.Success)
                    _positionOrderList.emit(
                        convertPositionWithCopierUseCase(
                            ConvertPositionItemWithCopier(
                                instrumentFilterId = id,
                                positions = result.data.positions ?: emptyList()
                            )
                        ).successOr(listOf(OrderPortfolioItem.EmptyItem())).toMutableList()
                    )
//                _positionState.emit(result.data?.positions ?: emptyList())
            }
        }
    }

    fun getCopierPosition(id: String) {
        viewModelScope.launch {
            getCopierUseCase(id).collect { result ->
                if (result is Result.Success)
                    _positionOrderList.emit(
                        convertGroupPositionToItemUseCase(
                            result.data.positions ?: emptyList()
                        ).successOr(emptyList())
                            .toMutableList()
                    )
                _copierState.emit(result.data)
            }
        }
    }

    fun getCardPosition() {
        viewModelScope.launch {
            getPositionMarketUseCase(Unit).collect { result ->
                val itemList = mutableListOf<OrderPortfolioItem>()
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
                _positionOrderList.emit(itemList)
            }
        }
    }

    fun getOrdersPosition() {
        viewModelScope.launch {
            getPendingOrdersUseCase(Unit).collect {
                val data = it.successOr(mutableListOf())
                _positionOrderList.emit(
                    convertOrderPositionToItemUseCase(data).successOr(listOf(OrderPortfolioItem.EmptyItem()))
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
                _positionOrderList.emit(items.toMutableList())
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
                _positionOrderList.emit(items.toMutableList())
            }
        }
    }
}