package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.exception.PositionExposureException
import com.awonar.android.exception.RateException
import com.awonar.android.model.order.*
import com.awonar.android.shared.domain.order.*
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.data
import com.molysulfur.library.result.succeeded
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
    private val calculateAmountStopLossWithBuyUseCase: CalculateAmountStopLossWithBuyUseCase
) : ViewModel() {

    private val _validateExposureErrorState: MutableSharedFlow<String?> = MutableSharedFlow()
    val validateExposureErrorState: MutableSharedFlow<String?> get() = _validateExposureErrorState

    private val _exposureState: MutableSharedFlow<Float> = MutableSharedFlow()
    val exposureState: MutableSharedFlow<Float> get() = _exposureState

    private val _getAmountState: MutableSharedFlow<Amount> = MutableSharedFlow()
    val getAmountState: SharedFlow<Amount> get() = _getAmountState

    private val _rateErrorMessageState: MutableSharedFlow<String?> = MutableSharedFlow()
    val rateErrorMessageState: SharedFlow<String?> get() = _rateErrorMessageState

    private val _minRateState: MutableSharedFlow<Float?> = MutableSharedFlow()
    val minRateState: SharedFlow<Float?> = _minRateState
    private val _maxRateState: MutableSharedFlow<Float?> = MutableSharedFlow()
    val maxRateState: SharedFlow<Float?> get() = _maxRateState

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

    fun getAmountOrUnit(
        instrumentId: Int,
        price: Float,
        amount: Amount,
        leverage: Int
    ) {
        viewModelScope.launch {
            val request = CalAmountUnitRequest(
                instrumentId = instrumentId,
                leverage = leverage,
                price = price,
                amount = if (amount.type == "amount") amount.unit else amount.amount
            )
            val newAmount = when (amount.type) {
                "amount" -> {
                    val result = getAmountUseCase(request)
                    amount.amount = result.successOr(amount.amount)
                    amount
                }
                else -> {
                    val result = getUnitUseCase(request)
                    amount.unit = result.successOr(amount.unit)
                    amount
                }
            }
            _getAmountState.emit(newAmount)
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
        number: Float,
        openPrice: Float,
        unitOrder: Float,
        type: MarketOrderType,
        orderType: String
    ) {
        val request = AmountStopLossRequest(
            instrumentId = instrumentId,
            stopLoss = number,
            openPrice = openPrice,
            unit = unitOrder
        )
        calculateAmountStopLossWithBuy(request)
    }

    private fun calculateAmountStopLossWithBuy(request: AmountStopLossRequest) {
        viewModelScope.launch {
            val result = calculateAmountStopLossWithBuyUseCase(request)
            if (result.succeeded) {
                Timber.e("${result.data}")
            }
        }
    }
}