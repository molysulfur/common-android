package com.awonar.app.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.exception.RateException
import com.awonar.android.model.order.CalAmountUnitRequest
import com.awonar.android.model.order.ValidateRateRequest
import com.awonar.android.shared.domain.order.GetAmountUseCase
import com.awonar.android.shared.domain.order.GetUnitUseCase
import com.awonar.android.shared.domain.order.ValidateMaxRateUseCase
import com.awonar.android.shared.domain.order.ValidateMinRateUseCase
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
    private val getAmountUseCase: GetAmountUseCase
) : ViewModel() {

    private val _getAmountState: MutableSharedFlow<Float> = MutableSharedFlow()
    val getAmountState: SharedFlow<Float> get() = _getAmountState

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

    fun getUnit(instrumentId: Int, price: Float, amount: Float, leverage: Int) {
        viewModelScope.launch {
            val result = getUnitUseCase(
                CalAmountUnitRequest(
                    instrumentId = instrumentId,
                    leverage = leverage,
                    price = price,
                    amount = amount,
                )
            )
            _getAmountState.emit(result.successOr(0f))
        }
    }

    fun getAmount(instrumentId: Int, price: Float, amount: Float, leverage: Int) {
        viewModelScope.launch {
            val result = getAmountUseCase(
                CalAmountUnitRequest(
                    instrumentId = instrumentId,
                    leverage = leverage,
                    price = price,
                    amount = amount
                )
            )
            _getAmountState.emit(result.successOr(0f))
        }
    }
}