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
    private val calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithSellUseCase: CalculateAmountStopLossAndTakeProfitWithSellUseCase,
    private val calculateRateStopLossAndTakeProfitWithBuyUseCase: CalculateRateStopLossAndTakeProfitWithBuyUseCase,
    private val calculateRateStopLossAndTakeProfitsWithSellUseCase: CalculateRateStopLossAndTakeProfitsWithSellUseCase,
    private val validateRateTakeProfitWithBuyUseCase: ValidateRateTakeProfitWithBuyUseCase,
    private val getOvernightFeeDaliyUseCase: GetOvernightFeeDaliyUseCase,
    private val getOvernightFeeWeeklyUseCase: GetOvernightFeeWeeklyUseCase,
    private val openOrderUseCase: OpenOrderUseCase
) : ViewModel() {

    private val _openOrderState = Channel<Boolean>(capacity = Channel.CONFLATED)
    val openOrderState get() = _openOrderState.receiveAsFlow()

    private val _overNightFeeState: MutableSharedFlow<Float> = MutableSharedFlow()
    val overNightFeeState: SharedFlow<Float> get() = _overNightFeeState
    private val _overNightFeeWeekState: MutableSharedFlow<Float> = MutableSharedFlow()
    val overNightFeeWeekState: SharedFlow<Float> get() = _overNightFeeWeekState

    private val _takeProfitState: MutableSharedFlow<Price> = MutableSharedFlow()
    val takeProfitState: SharedFlow<Price> get() = _takeProfitState

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