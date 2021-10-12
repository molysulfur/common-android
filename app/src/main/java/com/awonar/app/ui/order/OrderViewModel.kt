package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.constrant.TPSLType
import com.awonar.android.exception.PositionExposureException
import com.awonar.android.exception.RateException
import com.awonar.android.exception.ValidateStopLossException
import com.awonar.android.model.order.*
import com.awonar.android.shared.domain.order.*
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : ViewModel() {

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

    private val _minRateState: MutableSharedFlow<Float?> = MutableSharedFlow()
    val minRateState: SharedFlow<Float?> = _minRateState
    private val _maxRateState: MutableSharedFlow<Float?> = MutableSharedFlow()
    val maxRateState: SharedFlow<Float?> get() = _maxRateState

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

    fun calculateMaxRate(rate: Float, currentRate: Float) {
        viewModelScope.launch {
            val result: Result<Boolean?> = validateMaxRateUseCase(
                ValidateRateRequest(
                    rate = rate,
                    currentRate = currentRate
                )
            )
            if (result is Result.Error) {
                _maxRateState.emit((result.exception as RateException).rate)
            }
        }
    }

    fun calculateMinRate(rate: Float, currentRate: Float) {
        viewModelScope.launch {
            val result: Result<Boolean?> = validateMinRateUseCase(
                ValidateRateRequest(
                    rate = rate,
                    currentRate = currentRate
                )
            )
            if (result is Result.Error) {
                _minRateState.emit((result.exception as RateException).rate)
            }
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
            Timber.e("$result")
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
}