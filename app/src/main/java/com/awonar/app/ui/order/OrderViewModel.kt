package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.constrant.TPSLType
import com.awonar.android.exception.PositionExposureException
import com.awonar.android.exception.RateException
import com.awonar.android.exception.ValidateStopLossException
import com.awonar.android.model.order.*
import com.awonar.android.shared.domain.order.*
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val validateMinRateUseCase: ValidateMinRateUseCase,
    private val validateMaxRateUseCase: ValidateMaxRateUseCase,
    private val getUnitUseCase: GetUnitUseCase,
    private val getAmountUseCase: GetAmountUseCase,
    private val validateExposureUseCase: ValidateExposureUseCase,
    private val calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithSellUseCase: CalculateAmountStopLossAndTakeProfitWithSellUseCase,
    private val calculateRateStopLossAndTakeProfitWithBuyUseCase: CalculateRateStopLossAndTakeProfitWithBuyUseCase,
    private val calculateRateStopLossAndTakeProfitsWithSellUseCase: CalculateRateStopLossAndTakeProfitsWithSellUseCase,
    private val getDefaultStopLossUseCase: GetDefaultStopLossUseCase,
    private val getDefaultTakeProfitUseCase: GetDefaultTakeProfitUseCase,
    private val validateRateStopLossWithBuyUseCase: ValidateRateStopLossWithBuyUseCase,
    private val validateRateStopLossWithSellUseCase: ValidateRateStopLossWithSellUseCase,
    private val validateAmountStopLossWithBuyUseCase: ValidateAmountStopLossWithBuyUseCase,
    private val validateAmountStopLossWithSellUseCase: ValidateAmountStopLossWithSellUseCase,
    private val validateAmountStopLossWithNonLeverageBuyUseCase: ValidateAmountStopLossWithNonLeverageBuyUseCase,
    private val validateAmountStopLossWithNonLeverageSellUseCase: ValidateAmountStopLossWithNonLeverageSellUseCase,
    private val validateRateTakeProfitWithBuyUseCase: ValidateRateTakeProfitWithBuyUseCase,
    private val getOvernightFeeDaliyUseCase: GetOvernightFeeDaliyUseCase,
    private val getOvernightFeeWeeklyUseCase: GetOvernightFeeWeeklyUseCase,
    private val openOrderUseCase: OpenOrderUseCase
) : ViewModel() {

    private val _openOrderState= Channel<Boolean>(capacity = Channel.CONFLATED)
    val openOrderState get() = _openOrderState.receiveAsFlow()

    private val _overNightFeeState: MutableSharedFlow<Float> = MutableSharedFlow()
    val overNightFeeState: SharedFlow<Float> get() = _overNightFeeState
    private val _overNightFeeWeekState: MutableSharedFlow<Float> = MutableSharedFlow()
    val overNightFeeWeekState: SharedFlow<Float> get() = _overNightFeeWeekState

    private val _stopLossState: MutableSharedFlow<Price> = MutableSharedFlow()
    val stopLossState: SharedFlow<Price> get() = _stopLossState
    private val _takeProfitState: MutableSharedFlow<Price> = MutableSharedFlow()
    val takeProfitState: SharedFlow<Price> get() = _takeProfitState

    private val _validateExposureErrorState: MutableSharedFlow<String?> = MutableSharedFlow()
    val validateExposureErrorState: SharedFlow<String?> get() = _validateExposureErrorState

    private val _exposureState: MutableSharedFlow<Float> = MutableSharedFlow()
    val exposureState: MutableSharedFlow<Float> get() = _exposureState

    private val _getPriceState: MutableSharedFlow<Price> = MutableSharedFlow()
    val getPriceState: SharedFlow<Price> get() = _getPriceState

    fun openOrder(
        instrumentId: Int,
        amount: Price,
        stopLoss: Price,
        takeProfit: Price,
        orderType: String,
        leverage: Int,
        rate: Float
    ) {
        viewModelScope.launch {
            val request = OpenOrderRequest(
                instrumentId = instrumentId,
                amount = amount.amount,
                units = amount.unit,
                isBuy = orderType == "buy",
                leverage = leverage,
                rate = rate,
                stopLoss = stopLoss.unit,
                takeProfit = takeProfit.unit
            )
            openOrderUseCase(request).collect {
                val response = it.successOr(null)
                if (response != null) {
                    _openOrderState.send(true)
                } else {
                    _openOrderState.send(false)
                }
            }
        }
    }

    fun getDefaultStopLoss(instrumentId: Int, amount: Price) {
        viewModelScope.launch {
            val result = getDefaultStopLossUseCase(
                DefaultStopLossRequest(
                    instrumentId = instrumentId,
                    amount = amount.amount
                )
            )
            val price = Price(amount = result.successOr(0f), 1f, TPSLType.AMOUNT)
            _stopLossState.emit(price)
        }
    }

    fun getDefaultTakeProfit(instrumentId: Int, amount: Price) {
        viewModelScope.launch {
            val result = getDefaultTakeProfitUseCase(
                DefaultStopLossRequest(
                    instrumentId = instrumentId,
                    amount = amount.amount
                )
            )
            val newAmount = result.successOr(0f)
            _takeProfitState.emit(Price(amount = newAmount, 1f, TPSLType.AMOUNT))
        }
    }

    fun getUnit(
        instrumentId: Int,
        price: Float,
        amount: Price,
        leverage: Int
    ) {
        viewModelScope.launch {
            val request = CalAmountUnitRequest(
                instrumentId = instrumentId,
                leverage = leverage,
                price = price,
                amount = amount.amount
            )
            val result = getUnitUseCase(request)
            amount.unit = result.successOr(amount.unit)
            _getPriceState.emit(amount)
        }
    }

    fun getAmount(
        instrumentId: Int,
        price: Float,
        amount: Price,
        leverage: Int
    ) {
        viewModelScope.launch {
            val request = CalAmountUnitRequest(
                instrumentId = instrumentId,
                leverage = leverage,
                price = price,
                amount = amount.unit
            )
            val result = getAmountUseCase(request)
            amount.amount = result.successOr(amount.amount)
            _getPriceState.emit(amount)
        }
    }

    fun validatePositionExposure(
        id: Int,
        amount: Float,
        leverage: Int
    ) {
        viewModelScope.launch {
            val result = validateExposureUseCase(ExposureRequest(id, amount, leverage))
            if (result is Result.Error) {
                val exception: PositionExposureException =
                    (result.exception as PositionExposureException)
                _exposureState.emit(exception.value)
                _validateExposureErrorState.emit(exception.errorMessage)
            } else {
                _validateExposureErrorState.emit("")
            }

        }
    }

    fun changeTypeTakeProfit(
        instrumentId: Int,
        takeProfit: Price,
        takeProfitType: String?,
        openPrice: Float,
        unitOrder: Float,
        orderType: String?
    ) {
        viewModelScope.launch {
            takeProfit.type = takeProfitType ?: TPSLType.AMOUNT
            val request = StopLossRequest(
                instrumentId = instrumentId,
                stopLoss = takeProfit,
                openPrice = openPrice,
                unit = unitOrder
            )
            when (takeProfitType) {
                TPSLType.AMOUNT -> {
                    val result = when (orderType) {
                        "buy" -> calculateAmountStopLossAndTakeProfitWithBuyUseCase(request)
                        else -> calculateAmountStopLossAndTakeProfitWithSellUseCase(request)
                    }
                    _takeProfitState.emit(result.successOr(takeProfit))
                }
                TPSLType.RATE -> {
                    val result = when (orderType) {
                        "buy" -> calculateRateStopLossAndTakeProfitWithBuyUseCase(request)
                        else -> calculateRateStopLossAndTakeProfitsWithSellUseCase(request)
                    }
                    _takeProfitState.emit(result.successOr(takeProfit))
                }
            }
        }
    }

    fun changeTypeStopLoss(
        instrumentId: Int,
        stoploss: Price,
        stopLossType: String?,
        openPrice: Float,
        unitOrder: Float,
        orderType: String?
    ) {
        viewModelScope.launch {
            stoploss.type = stopLossType ?: TPSLType.AMOUNT
            val request = StopLossRequest(
                instrumentId = instrumentId,
                stopLoss = stoploss,
                openPrice = openPrice,
                unit = unitOrder
            )
            when (stopLossType) {
                TPSLType.AMOUNT -> {
                    val result = when (orderType) {
                        "buy" -> calculateAmountStopLossAndTakeProfitWithBuyUseCase(request)
                        else -> calculateAmountStopLossAndTakeProfitWithSellUseCase(request)
                    }
                    _stopLossState.emit(result.successOr(stoploss))
                }
                TPSLType.RATE -> {
                    val result = when (orderType) {
                        "buy" -> calculateRateStopLossAndTakeProfitWithBuyUseCase(request)
                        else -> calculateRateStopLossAndTakeProfitsWithSellUseCase(request)
                    }
                    _stopLossState.emit(result.successOr(stoploss))
                }
            }
        }
    }

    fun validateStopLoss(stoploss: Price, digit: Int, openPrice: Float, orderType: String) {
        viewModelScope.launch {
            val data = ValidateStopLossRequest(
                stopLoss = stoploss,
                digit = digit,
                openPrice = openPrice
            )
            validateRateStopLoss(data, orderType)
        }
    }

    fun validateStopLoss(instrumentId: Int, stoploss: Price, leverage: Int, type: String) {
        viewModelScope.launch {
            val data = ValidateAmountStopLossRequest(
                instrumentId = instrumentId,
                stopLoss = stoploss
            )
            validateAmountStopLoss(data, leverage, type)
        }
    }

    private suspend fun validateAmountStopLoss(
        data: ValidateAmountStopLossRequest,
        leverage: Int,
        type: String
    ) {
        val result = when {
            leverage == 1 && type == "buy" -> {
                validateAmountStopLossWithNonLeverageBuyUseCase(data)
            }
            leverage == 1 && type == "sell" -> {
                validateAmountStopLossWithNonLeverageSellUseCase(data)
            }
            leverage > 1 && type == "buy" -> {
                validateAmountStopLossWithBuyUseCase(data)
            }
            leverage > 1 && type == "sell" -> {
                validateAmountStopLossWithSellUseCase(data)
            }
            else -> {
                Result.Error(ValidateStopLossException("leverage or type was wrong!", 0f))
            }
        }
        if (result is Result.Error) {
            val exception = result.exception as ValidateStopLossException
            val stoploss = data.stopLoss
            stoploss.amount = exception.value
            _stopLossState.emit(stoploss)
        }
    }

    private suspend fun validateRateStopLoss(data: ValidateStopLossRequest, type: String) {
        val result = when (type) {
            "buy" -> validateRateStopLossWithBuyUseCase(
                data
            )
            "sell" -> validateRateStopLossWithSellUseCase(
                data
            )
            else -> Result.Error(ValidateStopLossException("type was wrong!", 0f))
        }
        when (result) {
            is Result.Error -> {
                val exception = result.exception as ValidateStopLossException
                val stoploss = data.stopLoss
                stoploss.unit = exception.value
                _stopLossState.emit(stoploss)
            }
        }
    }

    fun validateTakeProfit(takeProfit: Price, openPrice: Float, type: String) {
        viewModelScope.launch {
            val data = ValidateRateTakeProfitRequest(
                takeProfit = takeProfit,
                openPrice = openPrice
            )
            val result = when (type) {
                "buy" -> validateRateTakeProfitWithBuyUseCase(data)
                "sell" -> validateRateTakeProfitWithBuyUseCase(data)
                else -> Result.Error(ValidateStopLossException("type was wrong!", 0f))
            }
            when (result) {
                is Result.Error -> {
                    val exception = result.exception as ValidateStopLossException
                    val tp = data.takeProfit
                    tp.unit = exception.value
                    _takeProfitState.emit(tp)
                }
            }
        }
    }

    fun getOvernightFeeDaliy(instrumentId: Int, amount: Price, leverage: Int, orderType: String) {
        viewModelScope.launch {
            val result = getOvernightFeeDaliyUseCase(
                OvernightFeeRequest(
                    instrumentId = instrumentId,
                    amount = amount,
                    leverage = leverage,
                    orderType = orderType
                )
            )
            _overNightFeeState.emit(result.successOr(0f))
        }
    }

    fun getOvernightFeeWeek(instrumentId: Int, amount: Price, leverage: Int, orderType: String) {
        viewModelScope.launch {
            val result = getOvernightFeeWeeklyUseCase(
                OvernightFeeRequest(
                    instrumentId = instrumentId,
                    amount = amount,
                    leverage = leverage,
                    orderType = orderType
                )
            )
            _overNightFeeWeekState.emit(result.successOr(0f))
        }
    }
}