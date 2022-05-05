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
    private val getOvernightFeeDaliyUseCase: GetOvernightFeeDaliyUseCase,
    private val validateRateUseCase: ValidateRateUseCase,
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

    private val _buttonText = MutableStateFlow("Open Trade")
    val buttonText: StateFlow<String> get() = _buttonText

    private val _overNightFeeState: MutableStateFlow<Float> = MutableStateFlow(0f)
    val overNightFeeState: StateFlow<Float> get() = _overNightFeeState
    private val _overNightFeeWeekState: MutableStateFlow<Float> = MutableStateFlow(0f)
    val overNightFeeWeekState: StateFlow<Float> get() = _overNightFeeWeekState

    private val _amountError = MutableSharedFlow<String>()
    val amountError: SharedFlow<String> get() = _amountError


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

    fun validateRate(rate: Float, price: Float, digit: Int) {
        viewModelScope.launch {
            val result = validateRateUseCase(
                ValidateRateRequest(
                    rate = rate,
                    currentRate = price,
                    digit = digit
                )
            )
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