package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
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
    private val validateExposureUseCase: ValidateExposureUseCase,
) : ViewModel() {

    private val _isBuyState = MutableStateFlow<Boolean?>(null)
    private val _marketType = MutableStateFlow(MarketOrderType.ENTRY_ORDER)
    private val _portfolioState = MutableStateFlow<Portfolio?>(null)
    private val _exposureState = MutableStateFlow(0f)
    private val _takeProfitState = MutableStateFlow(Pair(0f, 0f))

    private val _priceState = MutableStateFlow(0f)
    val priceState get() = _priceState
    private val _changeState = MutableStateFlow(Pair(0f, 0f))
    val changeState get() = _changeState
    private val _marketStatus = MutableStateFlow("")
    val marketStatus get() = _marketStatus
    private val _instrument = MutableStateFlow<Instrument?>(null)
    val instrument get() = _instrument
    private val _tradingData = MutableStateFlow<TradingData?>(null)
    val tradingData get() = _tradingData
    private val _leverageState = MutableStateFlow(1)
    val leverageState: StateFlow<Int> get() = _leverageState

    private val _amountState = MutableStateFlow(Pair(0f, 0f))
    val amountState: StateFlow<Pair<Float, Float>> get() = _amountState

    private val _rateState = MutableStateFlow<Float?>(null)
    val rateState: StateFlow<Float?> get() = _rateState

    private val _hasPartialState = MutableStateFlow(false)
    val hasPartialState: StateFlow<Boolean> get() = _hasPartialState

    private val _openOrderState = Channel<String>(capacity = Channel.CONFLATED)
    val openOrderState get() = _openOrderState.receiveAsFlow()
    private val _orderSuccessState = Channel<String>(capacity = Channel.CONFLATED)
    val orderSuccessState get() = _orderSuccessState.receiveAsFlow()

    /**
     * first = amount
     * second = rate
     */
    private val _typeChangeState = MutableSharedFlow<Pair<Float, Float>>()
    val typeChangeState: SharedFlow<Pair<Float, Float>> get() = _typeChangeState
    private val _takeProfit = MutableStateFlow(Pair(0f, 0f))
    val takeProfit: StateFlow<Pair<Float, Float>> get() = _takeProfit
    private val _takeProfitError = MutableStateFlow("")
    val takeProfitError: StateFlow<String> get() = _takeProfitError

    private val _stopLossState = MutableStateFlow(Pair(0f, 0f))
    val stopLossState: StateFlow<Pair<Float, Float>> get() = _stopLossState
    private val _stopLossError = MutableStateFlow("")
    val stopLossError: StateFlow<String> get() = _stopLossError

    private val _errorState = MutableStateFlow("")
    val errorState: StateFlow<String> get() = _errorState
    private val _amountError = MutableStateFlow("")
    val amountError: StateFlow<String> get() = _amountError

    init {
        viewModelScope.launch {
            _takeProfitState.collect {
                val isBuy = _isBuyState.value == true
                if (_tradingData.value != null) {
                    val result = validateRateTakeProfitUseCase(
                        ValidateRateTakeProfitRequest(
                            rateTp = it.second,
                            currentPrice = priceState.value,
                            openPrice = rateState.value ?: 0f,
                            units = amountState.value.second,
                            isBuy = isBuy,
                            conversionRate = getConversionByInstrumentUseCase(
                                _instrument.value?.id
                                    ?: 0
                            ).successOr(0f),
                            amount = _amountState.value.first,
                            maxTakeProfitPercentage = _tradingData.value?.maxTakeProfitPercentage
                                ?: 0f,
                            digit = _instrument.value?.digit ?: 0
                        )
                    )
                    if (result is Result.Success) {
                        _takeProfit.value = it
                    }
                    if (result is Result.Error) {
                        setRateTp((result.exception as ValidationException).value)
                    }
                }
            }
        }
        viewModelScope.launch {
            _stopLossState.collect {
                val isBuy = _isBuyState.value == true
                val leverage = _leverageState.value
                if (_tradingData.value != null) {
                    val result = validateRateStopLossUseCase(
                        ValidateRateStopLossRequest(
                            amountSl = it.first,
                            rateSl = it.second,
                            amount = _amountState.value.first,
                            currentPrice = _priceState.value,
                            openPrice = _rateState.value ?: 0f,
                            exposure = _exposureState.value,
                            units = _amountState.value.second,
                            leverage = leverage,
                            isBuy = isBuy,
                            available = _portfolioState.value?.available ?: 0f,
                            conversionRate = getConversionByInstrumentUseCase(
                                _instrument.value?.id
                                    ?: 0
                            ).successOr(0f),
                            digit = _instrument.value?.digit ?: 0,
                            maxStopLoss = ConverterOrderUtil.getMaxAmountSl(
                                native = _stopLossState.value.first,
                                leverage = leverage,
                                isBuy = isBuy,
                                tradingData = tradingData.value
                            )
                        )
                    )
                    if (result is Result.Success) {
                        _stopLossState.value = it
                    }
                    if (result is Result.Error) {
                        if (result.exception is ValidationException) {
                            setRateSl((result.exception as ValidationException).value)
                        }
                    }
                }

            }
        }
        viewModelScope.launch {
            _leverageState.collect { leverage ->
                val result = validateExposureUseCase(
                    ExposureRequest(
                        instrumentId = _instrument.value?.id ?: 0,
                        amount = _amountState.value.first,
                        leverage = leverage
                    )
                )
                _exposureState.value = _amountState.value.first.times(leverage)
                if (result is Result.Error) {
                    _amountError.value = result.exception.message ?: ""
                }
            }
        }
        val initState = combine(
            _portfolioState,
            _instrument,
            _tradingData
        ) { portfolio, instrument, tradingData ->
            if (portfolio != null && instrument != null && tradingData != null) {
                /**
                 * Default Amount
                 */
                val leverage = tradingData.defaultLeverage
                val defaultAmount = portfolio.available.times(0.05f)
                val defaultUnit = getUnitUseCase(
                    CalAmountUnitRequest(
                        instrumentId = instrument.id,
                        leverage = leverage,
                        price = _priceState.value,
                        amount = defaultAmount
                    )
                ).successOr(0f)
                _amountState.emit(Pair(defaultAmount, defaultUnit))
                /**
                 * Default Leverage
                 */
                _leverageState.emit(leverage)
                /**
                 * Default SL, TP
                 */
                val conversionRate = getConversionByInstrumentUseCase(instrument.id).successOr(0f)
                val percent = tradingData.defaultStopLossPercentage.minus(0.5f).div(100)
                val amountStopLoss = defaultAmount.times(percent)
                val rateStopLoss = ConverterOrderUtil.convertAmountToRate(
                    amount = if (_isBuyState.value == true) -amountStopLoss else amountStopLoss,
                    conversionRate = conversionRate,
                    units = defaultUnit,
                    openRate = _priceState.value,
                    isBuy = _isBuyState.value == true
                )
                val defaultTakeProfit: Pair<Float, Float> = ConverterOrderUtil.getDefaultTakeProfit(
                    amount = defaultAmount,
                    defaultTakeProfitPercentage = tradingData.defaultTakeProfitPercentage,
                    conversionRate = conversionRate,
                    units = defaultUnit,
                    price = _priceState.value,
                    isBuy = _isBuyState.value == true
                )
                _stopLossState.value = Pair(-amountStopLoss, rateStopLoss)
                _takeProfitState.value = defaultTakeProfit
            }
        }

        viewModelScope.launch {
            initState.collect {

            }
        }
        viewModelScope.launch {
            getTradingDataByInstrumentIdUseCase(1)
        }
    }

    fun validateStopLoss(position: Position, current: Float) {
        if (current > 0) {
            viewModelScope.launch {
                getMyPortFolioUseCase(true).collectLatest {
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

    fun setAmountTp(
        amount: Float,
    ) {
        viewModelScope.launch {
            val conversionRate =
                getConversionByInstrumentUseCase(_instrument.value?.id ?: 0).successOr(0f)
            val tpRate = ConverterOrderUtil.convertAmountToRate(
                amount = amount,
                conversionRate = conversionRate,
                units = _amountState.value.second,
                openRate = rateState.value ?: 0f,
                isBuy = _isBuyState.value == true
            )
            _takeProfitState.value = Pair(amount, tpRate)
        }
    }

    fun setRateTp(
        tpRate: Float,
    ) {
        viewModelScope.launch {
            val conversionRate =
                getConversionByInstrumentUseCase(_instrument.value?.id ?: 0).successOr(0f)
            val tpAmount = ConverterOrderUtil.convertRateToAmount(
                rate = tpRate,
                conversionRate = conversionRate,
                units = _amountState.value.second,
                openRate = rateState.value ?: 0f,
                isBuy = _isBuyState.value == true
            )
            _takeProfitState.value = Pair(tpAmount, tpRate)
        }
    }

    fun setAmountSl(
        amount: Float,
    ) {
        viewModelScope.launch {
            val conversionRate =
                getConversionByInstrumentUseCase(_instrument.value?.id ?: 0).successOr(0f)
            val slRate = ConverterOrderUtil.convertAmountToRate(
                amount = amount,
                conversionRate = conversionRate,
                units = _amountState.value.second,
                openRate = rateState.value ?: 0f,
                isBuy = _isBuyState.value == true
            )
            _takeProfitState.value = Pair(amount, slRate)
        }
    }

    fun setRateSl(
        slRate: Float,
    ) {
        viewModelScope.launch {
            val conversionRate =
                getConversionByInstrumentUseCase(_instrument.value?.id ?: 0).successOr(0f)
            val slAmount = ConverterOrderUtil.convertRateToAmount(
                rate = slRate,
                conversionRate = conversionRate,
                units = _amountState.value.second,
                openRate = rateState.value ?: 0f,
                isBuy = _isBuyState.value == true
            )
            _takeProfitState.value = Pair(slAmount, slRate)
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
            id?.let {
                val tp = _takeProfitState.value.second
                val sl = stopLossState.value.second
                updateOrderUseCase(
                    UpdateOrderRequest(
                        id = id,
                        takeProfitRate = tp,
                        stopLossRate = sl
                    )
                ).collectLatest {
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
                closePositionUseCase(id).collectLatest {
                    if (it.succeeded) {
                        _orderSuccessState.send("Position was closed.")
                    }
                }
            } else {
                removePositionUseCase(id).collectLatest {
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
                ).collectLatest {
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
                ).collectLatest {
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
            val price = ConverterQuoteUtil.getCurrentPrice(
                quote = quote,
                leverage = _leverageState.value,
                isBuy = _isBuyState.value == true
            )
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

    fun submit() {
        viewModelScope.launch {
            val request = OpenOrderRequest(
                instrumentId = _instrument.value?.id ?: 0,
                amount = _amountState.value.first,
                isBuy = _isBuyState.value == true,
                leverage = _leverageState.value,
                rate = _rateState.value ?: 0f,
                stopLoss = _stopLossState.value.second,
                takeProfit = _takeProfit.value.second,
                units = _amountState.value.second
            )
            openOrderUseCase(request).collectLatest {
                if (it.succeeded) {
                    _openOrderState.send("Successfully")
                }
                if (it is Result.Error) {
                    _openOrderState.send("Failed to Open Trade")
                }
            }
        }
    }
}