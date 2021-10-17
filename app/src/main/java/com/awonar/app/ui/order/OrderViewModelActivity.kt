package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.exception.RateException
import com.awonar.android.model.order.CalAmountUnitRequest
import com.awonar.android.model.order.Price
import com.awonar.android.model.order.ValidateRateRequest
import com.awonar.android.shared.domain.order.GetUnitUseCase
import com.awonar.android.shared.domain.order.ValidateMaxRateUseCase
import com.awonar.android.shared.domain.order.ValidateMinRateUseCase
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModelActivity @Inject constructor(
    private val validateMinRateUseCase: ValidateMinRateUseCase,
    private val validateMaxRateUseCase: ValidateMaxRateUseCase,
    private val getUnitUseCase: GetUnitUseCase
) : ViewModel() {

    private val _amountState = MutableStateFlow(Price(0f, 0f, "amount"))
    val amountState: StateFlow<Price> get() = _amountState

    private val _rateState = MutableStateFlow(0f)
    val rateState: StateFlow<Float> get() = _rateState

    val marketOrderTypeState = MutableStateFlow(MarketOrderType.ENTRY_ORDER)

    fun getDefaultAmount(instrumentId: Int, leverage: Int, price: Float, available: Float) {
        viewModelScope.launch {
            val amount: Float = available.times(0.05f)
            val unitAmount = getUnitUseCase(
                CalAmountUnitRequest(
                    instrumentId = instrumentId,
                    leverage = leverage,
                    price = price,
                    amount = amount
                )
            )
            _amountState.value =
                _amountState.value.copy(amount = amount, unit = unitAmount.successOr(0f))
        }
    }

    fun calculateMaxRate(rate: Float, currentRate: Float, digit: Int) {
        viewModelScope.launch {
            val result: Result<Boolean?> = validateMaxRateUseCase(
                ValidateRateRequest(
                    rate = rate,
                    currentRate = currentRate,
                    digit = digit
                )
            )
            if (result is Result.Error) {
                _rateState.emit((result.exception as RateException).rate)
            }
        }
    }

    fun calculateMinRate(rate: Float, currentRate: Float, digit: Int) {
        viewModelScope.launch {
            val result: Result<Boolean?> = validateMinRateUseCase(
                ValidateRateRequest(
                    rate = rate,
                    currentRate = currentRate,
                    digit = digit
                )
            )
            if (result is Result.Error) {
                _rateState.emit((result.exception as RateException).rate)
            }
        }
    }

    fun updateMarketOrderType(type: MarketOrderType) {
        viewModelScope.launch {
            marketOrderTypeState.emit(type)
        }
    }

    fun updateRateWithStream(rate: Float) {
        viewModelScope.launch {
            if (marketOrderTypeState.value != MarketOrderType.PENDING_ORDER)
                _rateState.emit(rate)
        }
    }

    fun updateRate(rate: Float) {
        viewModelScope.launch {
            if (_rateState.value != rate) {
                _rateState.emit(rate)
            }
        }
    }

    fun updateAmountType(type: String) {
        _amountState.value = _amountState.value.copy(type = type)
    }
}