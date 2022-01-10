package com.awonar.app.dialog.copier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.exception.ValidationAmountCopyException
import com.awonar.android.exception.ValidationStopLossCopyException
import com.awonar.android.model.copier.CopiesRequest
import com.awonar.android.model.copier.ValidateCopyRequest
import com.awonar.android.model.socialtrade.Trader
import com.awonar.android.model.socialtrade.TradersRequest
import com.awonar.android.shared.domain.copy.*
import com.awonar.android.shared.domain.socialtrade.GetTradersUseCase
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CopyViewModel @Inject constructor(
    private val getRatioStopLossUseCase: GetRatioStopLossUseCase,
    private val getStopLossAmountCopyUseCase: GetStopLossAmountCopyUseCase,
    private val validationAmountCopyUseCase: ValidationAmountCopyUseCase,
    private val validateStoplossCopyUseCase: ValidateStoplossCopyUseCase,
    private val getTradersUseCase: GetTradersUseCase,
    private val createCopyUseCase: CreateCopyUseCase
) : ViewModel() {

    private val _isCopyExistState = MutableStateFlow(false)

    private val _traderState: MutableStateFlow<Trader?> = MutableStateFlow(null)
    val traderState: StateFlow<Trader?> get() = _traderState

    private val _amount: MutableStateFlow<Float> = MutableStateFlow(100f)
    val amount: StateFlow<Float> get() = _amount

    private val _amountError: MutableStateFlow<String> = MutableStateFlow("")
    val amountError: StateFlow<String> get() = _amountError

    private val _stopLoss: MutableStateFlow<Pair<Float, Float>> = MutableStateFlow(
        Pair(
            _amount.value.times(0.4f),
            _amount.value.times(0.4f).div(_amount.value)
        )
    )
    val stopLoss: StateFlow<Pair<Float, Float>> get() = _stopLoss
    private val _stopLossError: MutableStateFlow<String> = MutableStateFlow("")
    val stopLossError: StateFlow<String> get() = _stopLossError

    fun updateAmount(newAmount: Float) {
        _amount.value = newAmount
    }

    fun updateStopLoss(stopLoss: Float) {
        viewModelScope.launch {
            val newRatio = getRatioStopLossUseCase(Pair(_amount.value, stopLoss)).successOr(0f)
            _stopLoss.value = Pair(stopLoss, newRatio)
        }
    }

    fun updateRatio(ratio: Float) {
        viewModelScope.launch {
            val newStopLoss = getStopLossAmountCopyUseCase(Pair(_amount.value, ratio)).successOr(0f)
            _stopLoss.value = Pair(newStopLoss, ratio)
        }
    }

    fun validateAmount() {
        viewModelScope.launch {
            val result: Result<Unit> = validationAmountCopyUseCase(_amount.value)
            if (result is Result.Error) {
                val exception = result.exception as ValidationAmountCopyException
                _amountError.value = exception.errorMessage
                _amount.value = exception.value
            }
        }
    }

    fun validateStopLoss() {
        viewModelScope.launch {
            val result: Result<Unit> = validateStoplossCopyUseCase(
                ValidateCopyRequest(
                    amount = _amount.value,
                    stopLoss = _stopLoss.value.first
                )
            )
            if (result is Result.Error) {
                val exception = result.exception as ValidationStopLossCopyException
                _stopLossError.value = exception.errorMessage
                updateStopLoss(exception.value)
            }
        }
    }

    fun getTraderInfo(copiesId: String?) {
        copiesId?.let {
            viewModelScope.launch {
                getTradersUseCase(TradersRequest(uid = it, page = 1)).collect { result ->
                    val trader = result.successOr(null)?.get(0)
                    _traderState.value = trader
                }
            }
        }

    }

    fun updateIsCopyExist(isCopyExist: Boolean) {
        viewModelScope.launch {
            _isCopyExistState.value = isCopyExist
        }
    }

    fun submitCopy(copyId: String) {
        viewModelScope.launch {
            val request = CopiesRequest(
                copyExistingPositions = _isCopyExistState.value,
                stopLossPercentage = _stopLoss.value.first,
                initialInvestment = _amount.value,
                parentUserId = copyId
            )
            createCopyUseCase(request).collect {}
        }
    }

}