package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.exception.PositionExposureException
import com.awonar.android.exception.RateException
import com.awonar.android.exception.ValidationException
import com.awonar.android.model.order.*
import com.awonar.android.shared.domain.order.*
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModelActivity @Inject constructor(
    private val getUnitUseCase: GetUnitUseCase,
    private val getAmountUseCase: GetAmountUseCase,
    private val getOvernightFeeDaliyUseCase: GetOvernightFeeDaliyUseCase,
    private val getOvernightFeeWeeklyUseCase: GetOvernightFeeWeeklyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithSellUseCase: CalculateAmountStopLossAndTakeProfitWithSellUseCase,
    private val calculateRateStopLossAndTakeProfitWithBuyUseCase: CalculateRateStopLossAndTakeProfitWithBuyUseCase,
    private val calculateRateStopLossAndTakeProfitsWithSellUseCase: CalculateRateStopLossAndTakeProfitsWithSellUseCase,
    private val validateRateUseCase: ValidateRateUseCase,
    private val validateExposureUseCase: ValidateExposureUseCase,
    private val validateRateStopLossWithBuyUseCase: ValidateRateStopLossWithBuyUseCase,
    private val validateRateStopLossWithSellUseCase: ValidateRateStopLossWithSellUseCase,
    private val validateAmountStopLossWithBuyUseCase: ValidateAmountStopLossWithBuyUseCase,
    private val validateAmountStopLossWithSellUseCase: ValidateAmountStopLossWithSellUseCase,
    private val validateAmountStopLossWithNonLeverageBuyUseCase: ValidateAmountStopLossWithNonLeverageBuyUseCase,
    private val validateAmountStopLossWithNonLeverageSellUseCase: ValidateAmountStopLossWithNonLeverageSellUseCase,
) : ViewModel() {

    private val _rateErrorState = MutableStateFlow<RateException?>(null)
    val rateErrorState: StateFlow<RateException?> get() = _rateErrorState
    private val _exposureError = MutableStateFlow<PositionExposureException?>(null)
    val exposureError: StateFlow<PositionExposureException?> get() = _exposureError

    private val _getOrderRequest = Channel<OpenOrderRequest>(capacity = Channel.CONFLATED)
    val getOrderRequest get() = _getOrderRequest.receiveAsFlow()

    private val _buttonText = MutableStateFlow("Open Trade")
    val buttonText: StateFlow<String> get() = _buttonText
    private val _amountState = MutableStateFlow(Price(0f, 0f, "amount"))
    val amountState: StateFlow<Price> get() = _amountState
    private val _rateState = MutableStateFlow(0f)
    val rateState: StateFlow<Float> get() = _rateState
    private val _stopLossState = MutableStateFlow<Price?>(null)
    val stopLossState: StateFlow<Price?> get() = _stopLossState
    private val _takeProfit = MutableStateFlow<Price?>(null)
    val takeProfit: StateFlow<Price?> get() = _takeProfit
    private val _overNightFeeState: MutableStateFlow<Float> = MutableStateFlow(0f)
    val overNightFeeState: StateFlow<Float> get() = _overNightFeeState
    private val _overNightFeeWeekState: MutableStateFlow<Float> = MutableStateFlow(0f)
    val overNightFeeWeekState: StateFlow<Float> get() = _overNightFeeWeekState

    private val _amountError = MutableSharedFlow<String>()
    val amountError: SharedFlow<String> get() = _amountError

    fun getOvernightFeeDaliy(instrumentId: Int, orderType: String, leverage: Int) {
        viewModelScope.launch {
            val amount = _amountState.value
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


    private suspend fun validateAmountStopLoss(
        data: ValidateStopLossRequest,
        type: String,
        leverage: Int,
    ): Result<Price> {
        return when {
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
                Result.Error(ValidationException("leverage or type was wrong!", 0f))
            }
        }
    }

    private suspend fun validateRateStopLoss(
        data: ValidateStopLossRequest,
        type: String,
    ): Result<Price> = when (type) {
        "buy" -> validateRateStopLossWithBuyUseCase(
            data
        )
        "sell" -> validateRateStopLossWithSellUseCase(
            data
        )
        else -> Result.Error(ValidationException("type was wrong!", 0f))
    }

    fun updateStopLoss(number: Float, orderType: String, instrumentId: Int, openPrice: Float) {
        viewModelScope.launch {
            val stoploss = _stopLossState.value?.copy()
            when (stoploss?.type) {
                "amount" -> {
                    stoploss.amount = number * -1
                    getRateStopLoss(
                        stoploss,
                        instrumentId,
                        openPrice,
                        orderType
                    )
                }
                "rate" -> {
                    stoploss.unit = number
                    getAmountStopLoss(
                        stoploss,
                        instrumentId,
                        openPrice,
                        orderType
                    )
                }
                else -> null
            }

        }
    }

    private fun getAmountStopLoss(
        stoploss: Price,
        instrumentId: Int,
        openPrice: Float,
        orderType: String,
    ) {
        viewModelScope.launch {
            val amount = _amountState.value.copy()
            val result = when (orderType) {
                "sell" -> calculateAmountStopLossAndTakeProfitWithSellUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
                else -> calculateAmountStopLossAndTakeProfitWithBuyUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
            }

            if (result is Result.Success) {
                _stopLossState.emit(result.data)
            }
        }
    }

    private fun getRateStopLoss(
        stoploss: Price,
        instrumentId: Int,
        openPrice: Float,
        orderType: String,
    ) {
        viewModelScope.launch {
            val amount = _amountState.value.copy()
            val result = when (orderType) {
                "sell" -> calculateRateStopLossAndTakeProfitsWithSellUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
                else -> calculateRateStopLossAndTakeProfitWithBuyUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
            }
            if (result is Result.Success) {
                _stopLossState.emit(result.data)
            }
        }
    }

    fun validateRate(rate: Float, price: Float, digit: Int) {
        viewModelScope.launch {
            val result = validateRateUseCase(ValidateRateRequest(rate = rate,
                currentRate = price,
                digit = digit))
            when (result) {
                is Result.Success -> _rateErrorState.emit(null)
                is Result.Error -> {
                    val error = result.exception as RateException
                    _rateErrorState.emit(error)
                }
                else -> {}
            }
        }
    }

}