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
    private val validateRateTakeProfitWithBuyUseCase: ValidateRateTakeProfitWithBuyUseCase,
    private val openOrderUseCase: OpenOrderUseCase,
    private val calculateAmountTpSlUseCase: CalculateAmountTpSlUseCase
) : ViewModel() {

    private val _openOrderState = Channel<String>(capacity = Channel.CONFLATED)
    val openOrderState get() = _openOrderState.receiveAsFlow()

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
//                    _takeProfitState.emit(tp)
                }
            }
        }
    }

    private val _takeProfitState = MutableStateFlow(Pair(0f, 0f))
    val takeProfitState: StateFlow<Pair<Float, Float>> get() = _takeProfitState

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
                        current = current,
                        unit = unit,
                        instrumentId = instrumentId,
                        isBuy = isBuy
                    )
                    calculateAmountTpSlUseCase(request).successOr(Pair(first, second))
                }
                else -> {
                    first = tp
                    Pair(first, second)
                }
            }
            _takeProfitState.value = newTpSl
        }
    }


}