package com.awonar.app.ui.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.exception.MinRateException
import com.awonar.android.model.order.ValidateMinRateRequest
import com.awonar.android.shared.domain.order.GetTradingDataByInstrumentIdUseCase
import com.awonar.android.shared.domain.order.ValidateMinRateUseCase
import com.molysulfur.library.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val validateMinRateUseCase: ValidateMinRateUseCase
) : ViewModel() {

    private val _rateErrorMessageState: MutableSharedFlow<String?> = MutableSharedFlow()
    val rateErrorMessageState: SharedFlow<String?> get() = _rateErrorMessageState
    fun calculateMinRate(rate: Float, currentRate: Float) {
        viewModelScope.launch {
            val result: Result<Boolean?> = validateMinRateUseCase(
                ValidateMinRateRequest(
                    rate = rate,
                    currentRate = currentRate
                )
            )
            if (result is Result.Error) {
                _rateErrorMessageState.emit((result.exception as MinRateException).message)
            }
        }
    }
}