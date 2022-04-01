package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.exception.AddAmountException
import com.awonar.android.exception.RefundException
import com.awonar.android.exception.ValidationException
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.Quote
import com.awonar.android.model.order.*
import com.awonar.android.model.portfolio.Portfolio
import com.awonar.android.model.portfolio.Position
import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.shared.domain.market.GetConversionByInstrumentUseCase
import com.awonar.android.shared.domain.order.*
import com.awonar.android.shared.domain.partialclose.ClosePartialPositionUseCase
import com.awonar.android.shared.domain.partialclose.ClosePositionUseCase
import com.awonar.android.shared.domain.partialclose.RemovePositionUseCase
import com.awonar.android.shared.domain.partialclose.ValidatePartialCloseAmountUseCase
import com.awonar.android.shared.domain.portfolio.GetMyPortFolioUseCase
import com.awonar.android.shared.utils.ConverterOrderUtil
import com.awonar.android.shared.utils.ConverterQuoteUtil
import com.awonar.android.shared.utils.PortfolioUtil
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val getMyPortFolioUseCase: GetMyPortFolioUseCase,
    private val openOrderUseCase: OpenOrderUseCase,
    private val calculateAmountTpSlUseCase: CalculateAmountTpSlUseCase,
    private val calculateRateTpSlUseCase: CalculateRateTpSlUseCase,
    private val validateRateTakeProfitUseCase: ValidateRateTakeProfitUseCase,
    private val validateRateStopLossUseCase: ValidateRateStopLossUseCase,
    private val getConversionByInstrumentUseCase: GetConversionByInstrumentUseCase,
    private val getTradingDataByInstrumentIdUseCase: GetTradingDataByInstrumentIdUseCase,
    private val validatePartialCloseAmountUseCase: ValidatePartialCloseAmountUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase,
    private val getUnitUseCase: GetUnitUseCase,
    private val getAmountUseCase: GetAmountUseCase,
    private val validateVisiblePartialUseCase: ValidateVisiblePartialUseCase,
    private val closePositionUseCase: ClosePositionUseCase,
    private val closePartialPositionUseCase: ClosePartialPositionUseCase,
    private val removePositionUseCase: RemovePositionUseCase,
) : ViewModel() {

    private val _marketType = MutableStateFlow(MarketOrderType.ENTRY_ORDER)
    private val _portfolioState = MutableStateFlow<Portfolio?>(null)

    private val _priceState = MutableStateFlow(0f)
    val priceState get() = _priceState
    private val _changeState = MutableStateFlow(Pair(0f, 0f))
    val changeState get() = _changeState
    private val _marketStatus = MutableStateFlow<String>("")
    val marketStatus get() = _marketStatus
    private val _instrument = MutableStateFlow<Instrument?>(null)
    val instrument get() = _instrument
    private val _isBuyState = MutableSharedFlow<Boolean?>()
    val isBuyState get() = _isBuyState.asSharedFlow()
    private val _tradingData = MutableStateFlow<TradingData?>(null)
    val tradingData get() = _tradingData
    private val _leverageState = MutableStateFlow(1)
    val leverageState: StateFlow<Int> get() = _leverageState

    private val _amountState = MutableStateFlow(Pair(0f, 0f))
    val amountState: StateFlow<Pair<Float, Float>> get() = _amountState

    private val _openOrderState = Channel<String>(capacity = Channel.CONFLATED)
    val openOrderState get() = _openOrderState.receiveAsFlow()
    private val _orderSuccessState = Channel<String>(capacity = Channel.CONFLATED)
    val orderSuccessState get() = _orderSuccessState.receiveAsFlow()

    private val _rateState = MutableStateFlow<Float?>(null)
    val rateState: StateFlow<Float?> get() = _rateState

    private val _hasPartialState = MutableStateFlow(false)
    val hasPartialState: StateFlow<Boolean> get() = _hasPartialState

    /**
     * first = amount
     * second = rate
     */
    private val _typeChangeState = MutableSharedFlow<Pair<Float, Float>>()
    val typeChangeState: SharedFlow<Pair<Float, Float>> get() = _typeChangeState
    private val _takeProfitState = MutableStateFlow(Pair(0f, 0f))
    val takeProfitState: StateFlow<Pair<Float, Float>> get() = _takeProfitState
    private val _takeProfitError = MutableStateFlow("")
    val takeProfitError: StateFlow<String> get() = _takeProfitError

    private val _stopLossState = MutableStateFlow(Pair(0f, 0f))
    val stopLossState: StateFlow<Pair<Float, Float>> get() = _stopLossState
    private val _stopLossError = MutableStateFlow("")
    val stopLossError: StateFlow<String> get() = _stopLossError

    private val _errorState = MutableStateFlow("")
    val errorState: StateFlow<String> get() = _errorState


    init {
        val state = combine(_portfolioState,
            _instrument,
            _tradingData) { portfolio, instrument, tradingData ->
            if (portfolio != null && instrument != null && tradingData != null) {
                Timber.e("${instrument?.symbol} ${tradingData.defaultLeverage}")
                /**
                 * Default Amount
                 */
                val leverage = tradingData.defaultLeverage
                val defaultAmount = portfolio.available.times(0.05f)
                val defaultUnit = getUnitUseCase(CalAmountUnitRequest(
                    instrumentId = instrument.id,
                    leverage = leverage,
                    price = _priceState.value,
                    amount = defaultAmount
                )).successOr(0f)
                _amountState.emit(Pair(defaultAmount, defaultUnit))
                /**
                 * Default Leverage
                 */
                _leverageState.emit(leverage)
            }
        }

        viewModelScope.launch {
            state.collect {

            }
        }

        viewModelScope.launch {
            getTradingDataByInstrumentIdUseCase(1)
        }
        viewModelScope.launch {
            _portfolioState.collect { portfolio ->
                portfolio?.let {
//                    getTradingDataByInstrumentIdUseCase()
                    val amount = it.available.times(0.05)
                }
            }
        }
    }

    fun setDefaultAmount(instrumentId: Int, available: Float, price: Float) {
        viewModelScope.launch {
            val amount: Float = available.times(0.05f)
            val leverage: Int = leverageState.value
            val unitAmount: Float = getUnitUseCase(
                CalAmountUnitRequest(
                    instrumentId = instrumentId,
                    leverage = leverage,
                    price = price,
                    amount = amount
                )
            ).successOr(0f)
            _amountState.value = Pair(amount, unitAmount)
        }
    }

    fun openOrder(
        request: OpenOrderRequest,
    ) {
        viewModelScope.launch {
            openOrderUseCase(request).collect {
                if (it.succeeded) {
                    _openOrderState.send("Successfully")
                }
                if (it is Result.Error) {
                    _openOrderState.send("Failed to Open Trade")
                }
            }
        }
    }

    fun validateStopLoss(position: Position, current: Float) {
        if (current > 0) {
            viewModelScope.launch {
                getMyPortFolioUseCase(true).collect {
                    val conversionRate =
                        getConversionByInstrumentUseCase(position.instrument?.id ?: 0).successOr(1f)
                    val trading = getTradingDataByInstrumentIdUseCase(position.instrument?.id ?: 0)
                    val nativeAmount = position.exposure.div(position.leverage)
                    val stopLoss = stopLossState.value.copy()
                    val request = ValidateRateStopLossRequest(
                        rateSl = stopLoss.second,
                        amountSl = stopLoss.first,
                        currentPrice = current,
                        openPrice = position.openRate,
                        amount = position.amount,
                        units = position.units,
                        exposure = position.exposure,
                        leverage = position.leverage,
                        isBuy = position.isBuy,
                        available = it.data?.available ?: 0f,
                        conversionRate = conversionRate,
                        maxStopLoss = ConverterOrderUtil.getMaxAmountSl(
                            native = nativeAmount,
                            leverage = position.leverage,
                            isBuy = position.isBuy,
                            tradingData = trading.successOr(null)
                        ),
                        digit = position.instrument?.digit ?: 2
                    )
                    val result = validateRateStopLossUseCase(request)
                    when ((result as Result.Error).exception) {
                        is ValidationException -> {
                            setStopLoss(
                                sl = (result.exception as ValidationException).value,
                                type = "rate",
                                current = position.openRate,
                                unit = position.units,
                                instrumentId = position.instrument?.id ?: 0,
                                isBuy = position.isBuy
                            )
                            _stopLossError.emit(result.exception.message ?: "")
                        }
                        is AddAmountException -> {
                            _errorState.emit(result.exception.message ?: "")
                        }
                        is RefundException -> {
                            _errorState.emit(result.exception.message ?: "")
                        }
                    }
                }
            }
        }
    }

    fun validateTakeProfit(position: Position, current: Float) {
        if (current > 0) {
            viewModelScope.launch {
                val pl = PortfolioUtil.getProfitOrLoss(
                    current = current,
                    openRate = position.openRate,
                    unit = position.units,
                    rate = 1f,
                    isBuy = position.isBuy
                )
                position.instrument?.let { instrument ->
                    val data = ValidateRateTakeProfitRequest(
                        rateTp = takeProfitState.value.second,
                        currentPrice = current,
                        openPrice = position.openRate,
                        isBuy = position.isBuy,
                        value = pl.plus(position.amount),
                        units = position.units,
                        instrument = instrument
                    )
                    val result = validateRateTakeProfitUseCase(data)
                    if (result is Result.Error) {
                        val message = (result.exception as ValidationException).errorMessage
                        val rate = (result.exception as ValidationException).value
                        setTakeProfit(
                            tp = rate,
                            type = "rate",
                            current = position.openRate,
                            unit = position.units,
                            instrumentId = instrument.id,
                            isBuy = position.isBuy
                        )
                        _takeProfitError.emit(message)
                    }
                }
            }
        }
    }

    fun setTakeProfit(
        tp: Float,
        type: String? = null,
        current: Float,
        unit: Float,
        instrumentId: Int,
        isBuy: Boolean,
    ) {
        viewModelScope.launch {
            val state: Pair<Float, Float> = _takeProfitState.value.copy()

            /**
             * first = rate
             * second = amount
             */
            var first = state.first
            var second = state.second
            val newTpSl = when (type) {
                "rate" -> {
                    second = tp
                    val request = TpSlRequest(
                        tpsl = Pair(first, second),
                        openRate = current,
                        unit = unit,
                        instrumentId = instrumentId,
                        isBuy = isBuy
                    )
                    calculateAmountTpSlUseCase(request).successOr(Pair(first, second))
                }
                else -> {
                    first = tp
                    val request = TpSlRequest(
                        tpsl = Pair(first, second),
                        openRate = current,
                        unit = unit,
                        instrumentId = instrumentId,
                        isBuy = isBuy
                    )
                    calculateRateTpSlUseCase(request).successOr(Pair(first, second))
                }
            }
            _takeProfitState.emit(newTpSl)
        }
    }


    fun setStopLoss(
        sl: Float,
        type: String? = null,
        current: Float,
        unit: Float,
        instrumentId: Int,
        isBuy: Boolean,
    ) {
        viewModelScope.launch {
            val state: Pair<Float, Float> = _stopLossState.value.copy()

            /**
             * first = rate
             * second = amount
             */
            var first = state.first
            var second = state.second
            val newTpSl = when (type) {
                "rate" -> {
                    second = sl
                    val request = TpSlRequest(
                        tpsl = Pair(first, second),
                        openRate = current,
                        unit = unit,
                        instrumentId = instrumentId,
                        isBuy = isBuy
                    )
                    calculateAmountTpSlUseCase(request).successOr(Pair(first, second))
                }
                else -> {
                    first = sl
                    val request = TpSlRequest(
                        tpsl = Pair(first, second),
                        openRate = current,
                        unit = unit,
                        instrumentId = instrumentId,
                        isBuy = isBuy
                    )
                    calculateRateTpSlUseCase(request).successOr(Pair(first, second))
                }
            }
            _stopLossState.emit(newTpSl)
        }
    }


    fun edit(id: String?) {
        viewModelScope.launch {
            Timber.e("$id")
            id?.let {
                val tp = takeProfitState.value.second
                val sl = stopLossState.value.second
                updateOrderUseCase(
                    UpdateOrderRequest(
                        id = id,
                        takeProfitRate = tp,
                        stopLossRate = sl
                    )
                ).collect {
                    if (it.succeeded) {
                        _openOrderState.send("Update is successfully.")
                    }
                }
            }

        }
    }


    fun updateRate(rate: Float) {
        viewModelScope.launch {
            val newRate = when (_marketType.value) {
                MarketOrderType.ENTRY_ORDER, MarketOrderType.OPEN_ORDER -> priceState.value
                else -> rate
            }
            _rateState.emit(newRate)
        }
    }

    fun updateAmount(instrumentId: Int, amount: Float, price: Float) {
        viewModelScope.launch {
            val units = getUnitUseCase(
                CalAmountUnitRequest(
                    instrumentId = instrumentId,
                    leverage = _leverageState.value,
                    price = price,
                    amount = amount
                )
            ).successOr(0f)
            _amountState.value = Pair(amount, units)
        }
    }

    fun updateUnits(instrumentId: Int, units: Float, price: Float) {
        viewModelScope.launch {
            val amount = getAmountUseCase(
                CalAmountUnitRequest(
                    instrumentId = instrumentId,
                    leverage = _leverageState.value,
                    price = price,
                    amount = units
                )
            ).successOr(0f)
            _amountState.value = Pair(amount, units)
        }
    }

    fun toggleAmountType(checkedId: Int) {
        viewModelScope.launch {
            val amount = _amountState.value.copy()
            _typeChangeState.emit(amount)
        }
    }

    fun validatePartialCloseAmount(position: Position, price: Float) {
        viewModelScope.launch {
            val inputAmount = _amountState.value
            val rate = getConversionByInstrumentUseCase(position.instrument?.id ?: 0).successOr(0f)
            val pl = PortfolioUtil.getProfitOrLoss(
                current = price,
                openRate = position.openRate,
                unit = position.units,
                rate = rate,
                isBuy = position.isBuy
            )
            val result = validatePartialCloseAmountUseCase(
                ValidatePartialAmountRequest(
                    amount = position.amount,
                    inputAmount = inputAmount.first,
                    pl = pl,
                    units = position.units,
                    leverage = position.leverage,
                    id = position.instrument?.id ?: 0
                )
            )

            if (result is Result.Error) {
                val exception = result.exception as ValidationException
                updateAmount(position.instrument?.id ?: 0, exception.value, price)
            }
        }
    }


    fun setDefaultPartialAmount(position: Position, price: Float) {
        viewModelScope.launch {
            val tradingData =
                getTradingDataByInstrumentIdUseCase(position.instrument?.id ?: 0).successOr(null)
            val rate = getConversionByInstrumentUseCase(position.instrument?.id ?: 0).successOr(0f)
            val pl = PortfolioUtil.getProfitOrLoss(
                price,
                position.openRate,
                position.units,
                rate,
                position.isBuy
            )
            val value = PortfolioUtil.getValue(pl, position.amount)
            val leverage = position.leverage
            val minAmount =
                if (leverage < tradingData?.minLeverage ?: 0) tradingData?.minPositionExposure?.div(
                    position.leverage
                ) else tradingData?.minPositionAmount
            val defaultAmount =
                value.minus(minAmount ?: 0).times(0.5f)
            val hasPartial = validateVisiblePartialUseCase(
                HasPartialRequest(
                    amount = position.amount,
                    leverage = position.leverage,
                    id = position.instrument?.id ?: 0
                )
            ).successOr(false)
            _hasPartialState.value = hasPartial
            if (hasPartial) {
                updateAmount(position.instrument?.id ?: 0, defaultAmount, price)
            }
        }
    }

    suspend fun getProfit(price: Float, position: Position): Float {
        val rate = getConversionByInstrumentUseCase(position.instrument?.id ?: 0).successOr(0f)
        return PortfolioUtil.getProfitOrLoss(
            price,
            position.openRate,
            position.units,
            rate,
            position.isBuy
        )
    }

    fun closePosition(id: String, marketOrderType: MarketOrderType) {
        viewModelScope.launch {
            if (marketOrderType == MarketOrderType.PENDING_ORDER) {
                closePositionUseCase(id).collect {
                    if (it.succeeded) {
                        _orderSuccessState.send("Position was closed.")
                    }
                }
            } else {
                removePositionUseCase(id).collect {
                    _orderSuccessState.send("Position was closed.")
                }
            }
        }
    }

    fun closePartial(position: Position, current: Float, marketOrderType: MarketOrderType) {
        viewModelScope.launch {
            val unitReduct = _amountState.value.first.times(position.units).div(position.amount)
            val realAmountReduct = unitReduct.div(position.units).times(position.amount)
            if (marketOrderType == MarketOrderType.PENDING_ORDER) {
                closePartialPositionUseCase(
                    ExitOrderPartialRequest(
                        positionId = position.id,
                        unitsToDeduce = unitReduct,
                    )
                ).collect {
                    if (it.succeeded) {
                        _orderSuccessState.send(
                            "$realAmountReduct units of your %s position were closed at %s.".format(
                                position.instrument?.symbol,
                                current
                            )
                        )
                    }
                }
            } else {
                updateOrderUseCase(
                    UpdateOrderRequest(
                        id = position.id ?: "",
                        unitsToReduce = unitReduct
                    )
                ).collect {
                    if (it.succeeded) {
                        _orderSuccessState.send(
                            "$realAmountReduct units of your %s position were closed at %s.".format(
                                position.instrument?.symbol,
                                current
                            )
                        )
                    }
                }
            }
        }
    }

    fun updateLeverage(leverage: Int) {
        _leverageState.value = leverage
        _instrument.value?.let {
            updateAmount(
                instrumentId = it.id,
                amount = _amountState.value.first,
                price = _priceState.value
            )
        }

    }

    fun setPortfolio(portfolio: Portfolio) {
        _portfolioState.value = portfolio
    }

    fun setInstrument(instrument: Instrument?) {
        _instrument.value = instrument
    }

    fun setIsBuy(isBuy: Boolean?) {
        viewModelScope.launch {
            val currentType: Boolean? = isBuy
            val tradingData =
                getTradingDataByInstrumentIdUseCase(_instrument.value?.id ?: 0).successOr(null)
            _tradingData.value = tradingData
            if (tradingData?.allowSell == false && !tradingData.allowBuy && currentType != null) {
                _isBuyState.emit(null)
            } else {
                _isBuyState.emit(isBuy)
            }
        }
    }

    fun updatePrice(quotes: MutableMap<Int, Quote>) {
        val quote = quotes[_instrument.value?.id]
        quote?.let {
            _marketStatus.value = it.status ?: ""
            setPrice(quote)
        }
    }

    private fun setPrice(quote: Quote) {
        viewModelScope.launch {
            val price = ConverterQuoteUtil.getCurrentPrice(quote = quote,
                leverage = _leverageState.value,
                isBuy = _isBuyState.asLiveData().value == true)
            val change = ConverterQuoteUtil.change(price, quote.previous)
            val percentChange = ConverterQuoteUtil.percentChange(price, quote.previous)
            _changeState.value = Pair(change, percentChange)
            _priceState.value = price

        }
    }

    fun updateMarketType(openOrder: MarketOrderType) {
        when (openOrder) {
            MarketOrderType.PENDING_ORDER -> _marketType.value = MarketOrderType.PENDING_ORDER
            else -> {
                if (_marketStatus.value == "open") {
                    _marketType.value = MarketOrderType.OPEN_ORDER
                } else {
                    _marketType.value = MarketOrderType.ENTRY_ORDER
                }
            }
        }
    }


}