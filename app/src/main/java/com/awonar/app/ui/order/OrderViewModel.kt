package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val calculateAmountStopLossWithBuyUseCase: CalculateAmountStopLossWithBuyUseCase,
    private val calculateAmountStopLossWithSellUseCase: CalculateAmountStopLossWithSellUseCase,
    private val calculateRateStopLossWithBuyUseCase: CalculateRateStopLossWithBuyUseCase,
    private val calculateRateStopLossWithSellUseCase: CalculateRateStopLossWithSellUseCase,
    private val getDefaultStopLossUseCase: GetDefaultStopLossUseCase,
    private val validateStopLossUseCase: ValidateStopLossUseCase
) : ViewModel() {

    private val _stopLossState: MutableStateFlow<Price?> = MutableStateFlow(null)
    val stopLossState: StateFlow<Price?> get() = _stopLossState
    private val _stopLossErrorState: MutableSharedFlow<String> = MutableSharedFlow()
    val stopLossErrorState: SharedFlow<String> get() = _stopLossErrorState

    private val _takeProfitState: MutableSharedFlow<Price> = MutableSharedFlow()
    val takeProfitState: SharedFlow<Price> get() = _takeProfitState
    private val _takeProfitErrorState: MutableSharedFlow<String> = MutableSharedFlow()
    val takeProfitErrorState: SharedFlow<String> get() = _takeProfitErrorState

    private val _validateExposureErrorState: MutableSharedFlow<String?> = MutableSharedFlow()
    val validateExposureErrorState: SharedFlow<String?> get() = _validateExposureErrorState

    private val _exposureState: MutableSharedFlow<Float> = MutableSharedFlow()
    val exposureState: MutableSharedFlow<Float> get() = _exposureState

    private val _getPriceState: MutableSharedFlow<Price> = MutableSharedFlow()
    val getPriceState: SharedFlow<Price> get() = _getPriceState

    private val _rateErrorMessageState: MutableSharedFlow<String?> = MutableSharedFlow()
    val rateErrorMessageState: SharedFlow<String?> get() = _rateErrorMessageState

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
            val newAmount = result.successOr(0f)
            _stopLossState.emit(Price(amount = newAmount, 1f, "amount"))
        }
    }

    fun getDefaultTakeProfit(instrumentId: Int, amount: Price) {
        viewModelScope.launch {
            val result = getDefaultStopLossUseCase(
                DefaultStopLossRequest(
                    instrumentId = instrumentId,
                    amount = amount.amount
                )
            )
            val newAmount = result.successOr(0f)
            _takeProfitState.emit(Price(amount = newAmount, 1f, "amount"))
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
            Timber.e("$result")
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

    fun calculateStopLoss(
        instrumentId: Int,
        stoploss: Price,
        openPrice: Float,
        unitOrder: Float,
        orderType: String?
    ) {
        viewModelScope.launch {
            val request = StopLossRequest(
                instrumentId = instrumentId,
                stopLoss = stoploss,
                openPrice = openPrice,
                unit = unitOrder
            )
            when (stoploss.type) {
                "amount" -> {
                    val result = when (orderType) {
                        "buy" -> calculateAmountStopLossWithBuyUseCase(request)
                        else -> calculateAmountStopLossWithSellUseCase(request)
                    }
                    val newStopLoss = result.successOr(stoploss)
                    _stopLossState.emit(newStopLoss)
                }
                "rate" -> {
                    val result = when (orderType) {
                        "buy" -> calculateRateStopLossWithBuyUseCase(request)
                        else -> calculateRateStopLossWithSellUseCase(request)
                    }
                    val newStopLoss = result.successOr(stoploss)
                    _stopLossState.emit(newStopLoss)
                }
            }
        }
    }

    fun validateStopLoss(stoploss: Price, digit: Int, openPrice: Float) {
        viewModelScope.launch {
            val result = validateStopLossUseCase(
                ValidateStopLossRequest(
                    stopLoss = stoploss,
                    digit = digit,
                    openPrice = openPrice
                )
            )
            when (result) {
                is Result.Error -> {
                    val exception = result.exception as ValidateStopLossException
                    _stopLossErrorState.emit(exception.message ?: "")
                }
                is Result.Success -> {
                    _stopLossErrorState.emit("")
                }
                is Result.Loading -> {
                }
            }
        }
    }
}