package com.awonar.app.dialog.copier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.exception.ValidationAmountCopyException
import com.awonar.android.exception.ValidationStopLossCopyException
import com.awonar.android.model.copier.*
import com.awonar.android.model.portfolio.Copier
import com.awonar.android.model.socialtrade.Trader
import com.awonar.android.model.socialtrade.TradersRequest
import com.awonar.android.shared.domain.copy.*
import com.awonar.android.shared.domain.portfolio.GetMyPortFolioUseCase
import com.awonar.android.shared.domain.socialtrade.GetTradersUseCase
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.succeeded
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class CopyViewModel @Inject constructor(
    private val getRatioStopLossUseCase: GetRatioStopLossUseCase,
    private val getStopLossAmountCopyUseCase: GetStopLossAmountCopyUseCase,
    private val validationAmountCopyUseCase: ValidationAmountCopyUseCase,
    private val validateStoplossCopyUseCase: ValidateStoplossCopyUseCase,
    private val getTradersUseCase: GetTradersUseCase,
    private val createCopyUseCase: CreateCopyUseCase,
    private val stopCopyUseCase: StopCopyUseCase,
    private val getMyPortFolioUseCase: GetMyPortFolioUseCase,
    private val updateFundUseCase: UpdateFundUseCase,
    private val validateRemoveFundUseCase: ValidateRemoveFundUseCase,
    private val updatePauseCopyUseCase: UpdatePauseCopyUseCase,
) : ViewModel() {

    private val _messageState = Channel<String>(Channel.CONFLATED)
    val messageState: Flow<String> get() = _messageState.receiveAsFlow()

    private val _errorState = Channel<String>(Channel.CONFLATED)
    val errorState: Flow<String> get() = _errorState.receiveAsFlow()

    private val _isCopyExistState = MutableStateFlow(false)

    private val _traderState: MutableStateFlow<Trader?> = MutableStateFlow(null)
    val traderState: StateFlow<Trader?> get() = _traderState

    private val _amount: MutableStateFlow<Float> = MutableStateFlow(0f)
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
    private val _stopLossError: MutableStateFlow<Pair<String, String>> = MutableStateFlow(
        Pair("", "Min 5.00%, Max 95.00%")
    )
    val stopLossError: StateFlow<Pair<String, String>> get() = _stopLossError

    init {
        viewModelScope.launch {
            getMyPortFolioUseCase(true).collect {
                val available = it.successOr(null)?.available ?: 0f
                val allocate = it.successOr(null)?.totalAllocated ?: 0f
                val MAX_AMOUNT = 100000f.minus(allocate)
                val MIN_AMOUNT = 100f
                _stopLossError.emit(Pair("Min $%.2f, Max $%.2f".format(MIN_AMOUNT, MAX_AMOUNT),
                    _stopLossError.value.second))
                _amount.emit(available.times(0.05f))
            }
        }
    }

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
                updateStopLoss(exception.value)
            }
        }
    }

    fun getTraderInfo(copiesId: String?) {
        copiesId?.let {
            viewModelScope.launch {
                getTradersUseCase(TradersRequest(uid = it,
                    page = 1,
                    maxRisk = 10)).collect { result ->
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

    fun stopCopy(id: String) {
        viewModelScope.launch {
            stopCopyUseCase(id).collect {
                if (it.succeeded) {
                    _messageState.send("Successful.")
                }

                if (it is Result.Error) {
                    _errorState.send(it.exception.message ?: "")
                }

            }
        }
    }

    fun addFund(copyId: String) {
        viewModelScope.launch {
            val request = UpdateFundRequest(
                amount = _amount.value,
                id = copyId
            )
            updateFundUseCase(request).collect {
                if (it.succeeded) {
                    _messageState.send("Successful.")
                }

                if (it is Result.Error) {
                    _errorState.send(it.exception.message ?: "")
                }
            }
        }
    }

    fun validateRemoveAmount(copier: Copier) {
        viewModelScope.launch {
            val result = validateRemoveFundUseCase(
                ValidateRemoveFundRequest(
                    copier,
                    _amount.value
                )
            )

            if (result is Result.Error) {
                _amountError.value = result.exception.message ?: ""
            }
        }
    }

    fun removeFund(copyId: String) {
        viewModelScope.launch {
            val request = UpdateFundRequest(
                amount = -abs(_amount.value),
                id = copyId
            )
            updateFundUseCase(request).collect {
                if (it.succeeded) {
                    _messageState.send("Successful.")
                }

                if (it is Result.Error) {
                    _errorState.send(it.exception.message ?: "")
                }
            }
        }
    }

    fun pauseCopy(copyId: String, isPause: Boolean) {
        viewModelScope.launch {
            updatePauseCopyUseCase(
                PauseCopyRequest(
                    copyId,
                    isPause
                )
            ).collect {
                if (it.succeeded) {
                    _messageState.send("Successfully.")
                }

                if (it is Result.Error) {
                    _errorState.send(it.exception.message ?: "")
                }
            }
        }
    }

}