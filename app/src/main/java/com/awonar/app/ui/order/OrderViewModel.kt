package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.exception.AddAmountException
import com.awonar.android.exception.RefundException
import com.awonar.android.exception.ValidationException
import com.awonar.android.model.order.*
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.domain.market.GetConversionByInstrumentUseCase
import com.awonar.android.shared.domain.order.*
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
    private val updateOrderUseCase: UpdateOrderUseCase
) : ViewModel() {

    private val _openOrderState = Channel<String>(capacity = Channel.CONFLATED)
    val openOrderState get() = _openOrderState.receiveAsFlow()

    /**
     * first = amount
     * second = rate
     */
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
                    val message = it.successOr("")
                    if (!message.isNullOrBlank()) {
                        _openOrderState.send(message)
                    }
                }
            }

        }
    }

}