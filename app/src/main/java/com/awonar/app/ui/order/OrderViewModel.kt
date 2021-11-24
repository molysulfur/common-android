package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.exception.ValidationException
import com.awonar.android.model.order.*
import com.awonar.android.model.portfolio.Position
import com.awonar.android.shared.domain.order.*
import com.awonar.android.shared.utils.PortfolioUtil
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
    private val openOrderUseCase: OpenOrderUseCase,
    private val calculateAmountTpSlUseCase: CalculateAmountTpSlUseCase,
    private val calculateRateTpSlUseCase: CalculateRateTpSlUseCase,
    private val validateRateTakeProfitUseCase: ValidateRateTakeProfitUseCase
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

    fun validateTakeProfit(position: Position, current: Float, isBuy: Boolean) {
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
                isBuy = isBuy,
                value = pl.plus(position.amount),
                units = position.units,
                instrument = position.instrument
            )
            val result = validateRateTakeProfitUseCase(data)
            if (result is Result.Error) {
                val message = (result.exception as ValidationException).errorMessage
                val tpRate = (result.exception as ValidationException).value
                setTakeProfit(
                    tp = tpRate,
                    type = "rate",
                    current = current,
                    unit = position.units,
                    instrumentId = position.instrument.id,
                    isBuy = position.isBuy
                )
                _takeProfitError.emit(message)
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
                        current = current,
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
                        current = current,
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


}