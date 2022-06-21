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
    private val validateExposureUseCase: ValidateExposureUseCase,
    private val validateRateUseCase: ValidateRateUseCase,
    private val validateRateStopLossWithBuyUseCase: ValidateRateStopLossWithBuyUseCase,
    private val validateRateStopLossWithSellUseCase: ValidateRateStopLossWithSellUseCase,
    private val validateAmountStopLossWithBuyUseCase: ValidateAmountStopLossWithBuyUseCase,
    private val validateAmountStopLossWithSellUseCase: ValidateAmountStopLossWithSellUseCase,
    private val validateAmountStopLossWithNonLeverageBuyUseCase: ValidateAmountStopLossWithNonLeverageBuyUseCase,
    private val validateAmountStopLossWithNonLeverageSellUseCase: ValidateAmountStopLossWithNonLeverageSellUseCase,
) : ViewModel() {

    private val _showAmount = MutableStateFlow(true)
    val showAmount = _showAmount
    private val _showAmountTp = MutableStateFlow(true)
    val showAmountTp = _showAmountTp
    private val _showAmountSl = MutableStateFlow(true)
    val showAmountSl = _showAmountSl
    private val _isSetRate = MutableStateFlow(false)
    val isSetRate = _isSetRate

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

    fun toggleShowAmount() {
        _showAmount.value = !_showAmount.value
    }

    fun toggleShowTp() {
        _showAmountTp.value = !_showAmountTp.value
    }

    fun toggleShowSl() {
        _showAmountSl.value = !_showAmountSl.value
    }

    fun setAtMarket(isAtMarket: Boolean) {
        _isSetRate.value = isAtMarket
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

    fun validateExposure(id: Int, amount: Float, leverage: Int) {
        viewModelScope.launch {
            val result = validateExposureUseCase(
                ExposureRequest(
                    instrumentId = id,
                    amount = amount,
                    leverage = leverage
                )
            )
            when (result) {
                is Result.Success -> _exposureError.emit(null)
                is Result.Error -> {
                    _exposureError.emit(result.exception as PositionExposureException)
                }
                else -> {}
            }
        }
    }

}