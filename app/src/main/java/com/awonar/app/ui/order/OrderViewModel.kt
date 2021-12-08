package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.exception.AddAmountException
import com.awonar.android.exception.PositionExposureException
import com.awonar.android.exception.RefundException
import com.awonar.android.exception.ValidationException
import com.awonar.android.model.order.*
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.domain.market.GetConversionByInstrumentUseCase
import com.awonar.android.shared.domain.order.*
import com.awonar.android.shared.domain.partialclose.ClosePartialPositionUseCase
import com.awonar.android.shared.domain.partialclose.ClosePositionUseCase
import com.awonar.android.shared.domain.partialclose.RemovePositionUseCase
import com.awonar.android.shared.domain.partialclose.ValidatePartialCloseAmountUseCase
import com.awonar.android.shared.domain.portfolio.GetMyPortFolioUseCase
import com.awonar.android.shared.utils.ConverterOrderUtil
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
    private val validateExposureUseCase: ValidateExposureUseCase,
    private val validatePartialCloseAmountUseCase: ValidatePartialCloseAmountUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase,
    private val getUnitUseCase: GetUnitUseCase,
    private val getAmountUseCase: GetAmountUseCase,
    private val validateVisiblePartialUseCase: ValidateVisiblePartialUseCase,
    private val closePositionUseCase: ClosePositionUseCase,
    private val closePartialPositionUseCase: ClosePartialPositionUseCase,
    private val removePositionUseCase: RemovePositionUseCase
) : ViewModel() {

    private val _openOrderState = Channel<String>(capacity = Channel.CONFLATED)
    val openOrderState get() = _openOrderState.receiveAsFlow()
    private val _orderSuccessState = Channel<String>(capacity = Channel.CONFLATED)
    val orderSuccessState get() = _orderSuccessState.receiveAsFlow()

    private val _exposureState = MutableStateFlow<Float>(0f)
    val exposureState: StateFlow<Float> get() = _exposureState
    private val _exposureError = MutableStateFlow("")
    val exposureError: StateFlow<String> get() = _exposureError

    private val _openRate = MutableStateFlow<Float?>(null)
    val openRate: StateFlow<Float?> get() = _openRate

    private val _amountState = MutableStateFlow(Pair(0f, 0f))
    val amountState: StateFlow<Pair<Float, Float>> get() = _amountState
    private val _amountError = MutableStateFlow("")
    val amountError: StateFlow<String> get() = _amountError
    private val _leverageState = MutableStateFlow(1)
    val leverageState: StateFlow<Int> get() = _leverageState

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

    fun validatePositionExposure(instrumentId: Int) {
        viewModelScope.launch {
            val amount = _amountState.value
            val leverage = leverageState.value
            if (leverage > 0) {
                val result =
                    validateExposureUseCase(ExposureRequest(instrumentId, amount.first, leverage))
                if (result is Result.Error) {
                    val exception: PositionExposureException =
                        (result.exception as PositionExposureException)
                    _exposureState.emit(exception.value)
                    _exposureError.emit(exception.message ?: "")
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
        request: OpenOrderRequest
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
                        getConversionByInstrumentUseCase(position.instrument.id).successOr(1f)
                    val trading = getTradingDataByInstrumentIdUseCase(position.instrument.id)
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
                        digit = position.instrument.digit
                    )
                    val result = validateRateStopLossUseCase(request)
                    when ((result as Result.Error).exception) {
                        is ValidationException -> {
                            setStopLoss(
                                sl = (result.exception as ValidationException).value,
                                type = "rate",
                                current = position.openRate,
                                unit = position.units,
                                instrumentId = position.instrument.id,
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
                val data = ValidateRateTakeProfitRequest(
                    rateTp = takeProfitState.value.second,
                    currentPrice = current,
                    openPrice = position.openRate,
                    isBuy = position.isBuy,
                    value = pl.plus(position.amount),
                    units = position.units,
                    instrument = position.instrument
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
                        instrumentId = position.instrument.id,
                        isBuy = position.isBuy
                    )
                    _takeProfitError.emit(message)
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
        isBuy: Boolean
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
        isBuy: Boolean
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

    fun updateRate(price: Float, marketType: MarketOrderType) {
        viewModelScope.launch {
            if (marketType == MarketOrderType.ENTRY_ORDER || marketType == MarketOrderType.OPEN_ORDER) {
                _openRate.emit(price)
            }
        }
    }

    fun updateAmount(instrumentId: Int, amount: Float, leverage: Int, price: Float) {
        viewModelScope.launch {
            val units = getUnitUseCase(
                CalAmountUnitRequest(
                    instrumentId = instrumentId,
                    leverage = leverage,
                    price = price,
                    amount = amount
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
            val rate = getConversionByInstrumentUseCase(position.instrument.id).successOr(0f)
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
                    id = position.instrument.id
                )
            )

            if (result is Result.Error) {
                val exception = result.exception as ValidationException
                _amountError.value = exception.message ?: ""
                updateAmount(position.instrument.id, exception.value, position.leverage, price)
            }
        }
    }

    fun updateUnits(id: Int, units: Float, leverage: Int, current: Float) {
        viewModelScope.launch {
            val amount = getAmountUseCase(
                CalAmountUnitRequest(
                    instrumentId = id,
                    leverage = leverage,
                    price = current,
                    amount = units
                )
            ).successOr(0f)
            _amountState.value = Pair(amount, units)
        }
    }

    fun setDefaultPartialAmount(position: Position, price: Float) {
        viewModelScope.launch {
            val tradingData =
                getTradingDataByInstrumentIdUseCase(position.instrument.id).successOr(null)
            val rate = getConversionByInstrumentUseCase(position.instrument.id).successOr(0f)
            val pl = PortfolioUtil.getProfitOrLoss(
                price,
                position.openRate,
                position.units,
                rate,
                position.isBuy
            )
            val value = PortfolioUtil.getValue(pl, position.amount)

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
                    id = position.instrument.id
                )
            ).successOr(false)
            _hasPartialState.value = hasPartial
            if (hasPartial) {
                updateAmount(position.instrument.id, defaultAmount, position.leverage, price)
            }
        }
    }

    suspend fun getProfit(price: Float, position: Position): Float {
        val rate = getConversionByInstrumentUseCase(position.instrument.id).successOr(0f)
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

    fun closePartial(position: Position, marketOrderType: MarketOrderType) {
        viewModelScope.launch {
            val unit = _amountState.value.second
            val unitReduct = _amountState.value.first.times(position.amount)
            val realAmountReduct = unitReduct.div(unit).times(position.amount)
            if (marketOrderType == MarketOrderType.PENDING_ORDER) {
                closePartialPositionUseCase(
                    ExitOrderPartialRequest(
                        positionId = position.id,
                        unitsToDeduce = position.units,
                    )
                ).collect {
                    if (it.succeeded) {
                        _orderSuccessState.send("$realAmountReduct unit of your buy position were closed.")
                    }
                }
            } else {
                updateOrderUseCase(
                    UpdateOrderRequest(
                        id = position.id,
                        unitsToReduce = unit
                    )
                ).collect {
                    if (it.succeeded) {
                        _orderSuccessState.send("$realAmountReduct unit of your buy position were closed.")
                    }
                }
            }
        }
    }

}