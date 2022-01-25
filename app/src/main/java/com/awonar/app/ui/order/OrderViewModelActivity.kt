package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.exception.PositionExposureException
import com.awonar.android.exception.RateException
import com.awonar.android.exception.ValidationException
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.order.*
import com.awonar.android.shared.domain.order.*
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrderViewModelActivity @Inject constructor(
    private val getUnitUseCase: GetUnitUseCase,
    private val getAmountUseCase: GetAmountUseCase,
    private val getDefaultStopLossUseCase: GetDefaultStopLossUseCase,
    private val getDefaultTakeProfitUseCase: GetDefaultTakeProfitUseCase,
    private val getOvernightFeeDaliyUseCase: GetOvernightFeeDaliyUseCase,
    private val getOvernightFeeWeeklyUseCase: GetOvernightFeeWeeklyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithSellUseCase: CalculateAmountStopLossAndTakeProfitWithSellUseCase,
    private val calculateRateStopLossAndTakeProfitWithBuyUseCase: CalculateRateStopLossAndTakeProfitWithBuyUseCase,
    private val calculateRateStopLossAndTakeProfitsWithSellUseCase: CalculateRateStopLossAndTakeProfitsWithSellUseCase,
    private val validateRateUseCase: ValidateRateUseCase,
    private val validateExposureUseCase: ValidateExposureUseCase,
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

    private val _getOrderRequest = Channel<OpenOrderRequest>(capacity = Channel.CONFLATED)
    val getOrderRequest get() = _getOrderRequest.receiveAsFlow()


    private val _detailState = MutableStateFlow("")
    val detailState: StateFlow<String> get() = _detailState
    private val _buttonText = MutableStateFlow("Open Trade")
    val buttonText: StateFlow<String> get() = _buttonText
    private val _amountState = MutableStateFlow(Price(0f, 0f, "amount"))
    val amountState: StateFlow<Price> get() = _amountState
    private val _rateState = MutableStateFlow(0f)
    val rateState: StateFlow<Float> get() = _rateState
    private val _stopLossState = MutableStateFlow<Price?>(null)
    val stopLossState: StateFlow<Price?> get() = _stopLossState
    private val _takeProfit = MutableStateFlow<Price?>(null)
    val takeProfit: StateFlow<Price?> get() = _takeProfit
    private val _overNightFeeState: MutableStateFlow<Float> = MutableStateFlow(0f)
    val overNightFeeState: StateFlow<Float> get() = _overNightFeeState
    private val _overNightFeeWeekState: MutableStateFlow<Float> = MutableStateFlow(0f)
    val overNightFeeWeekState: StateFlow<Float> get() = _overNightFeeWeekState

    private val _exposureState = MutableSharedFlow<Float>()
    val exposureState: SharedFlow<Float> get() = _exposureState

    val marketOrderTypeState = MutableStateFlow(MarketOrderType.ENTRY_ORDER)

    fun getOvernightFeeDaliy(instrumentId: Int, orderType: String, leverage: Int) {
        viewModelScope.launch {
            val amount = _amountState.value
            val result = getOvernightFeeDaliyUseCase(
                OvernightFeeRequest(
                    instrumentId = instrumentId,
                    amount = amount,
                    leverage = leverage,
                    orderType = orderType
                )
            )
            _overNightFeeState.emit(result.successOr(0f))
        }
    }

    fun getOvernightFeeWeek(instrumentId: Int, orderType: String) {
//        viewModelScope.launch {
//            val amount = _amountState.value
//            val leverage = leverageState.value
//            val result = getOvernightFeeWeeklyUseCase(
//                OvernightFeeRequest(
//                    instrumentId = instrumentId,
//                    amount = amount,
//                    leverage = leverage,
//                    orderType = orderType
//                )
//            )
//            _overNightFeeWeekState.emit(result.successOr(0f))
//        }
    }

    fun validateStopLoss(
        instrument: Instrument,
        openPrice: Float,
        orderType: String,
        leverage: Int,
    ) {
        viewModelScope.launch {
            val stoploss = _stopLossState.value?.copy()
            val amount = _amountState.value
            stoploss?.let {
                val data = ValidateStopLossRequest(
                    instrumentId = instrument.id,
                    stopLoss = stoploss,
                    amount = amount.amount,
                    digit = instrument.digit,
                    openPrice = openPrice
                )
                val result: Result<Price>? = when (stoploss.type) {
                    "amount" -> validateAmountStopLoss(data, orderType, leverage)
                    "rate" -> validateRateStopLoss(data, orderType)
                    else -> null
                }
                if (result is Result.Error && result.exception is ValidationException) {
                    val exception = result.exception as ValidationException
                    updateStopLoss(exception.value, orderType, instrument.id, openPrice)
                }
            }

        }
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

    fun updateStopLossType(type: String) {
        viewModelScope.launch {
            _stopLossState.value = _stopLossState.value?.copy(type = type)
        }
    }

    fun getDefaultTakeProfit(instrumentId: Int, price: Float, orderType: String) {
        viewModelScope.launch {
            val amount = _amountState.value
            val resultTp = getDefaultTakeProfitUseCase(
                DefaultStopLossRequest(
                    instrumentId = instrumentId,
                    amount = amount.amount,
                    price = price,
                    unit = amount.unit
                )
            )
            if (resultTp is Result.Success) {
                resultTp.data.let { stoploss ->
                    getRateTakeProfit(
                        stoploss,
                        instrumentId,
                        price,
                        orderType
                    )
                }
            }

        }
    }

    fun getDefaultStopLoss(instrumentId: Int, price: Float, orderType: String) {
        viewModelScope.launch {
            val amount = _amountState.value
            if (amount.amount > 0) {
                val resultSL = getDefaultStopLossUseCase(
                    DefaultStopLossRequest(
                        instrumentId = instrumentId,
                        amount = amount.amount,
                        price = price,
                        unit = amount.unit
                    )
                )
                if (resultSL is Result.Success) {
                    resultSL.data.let { stoploss ->
                        getRateStopLoss(
                            stoploss,
                            instrumentId,
                            price,
                            orderType
                        )
                    }
                }
            }
        }
    }

    fun validatePositionExposure(
        id: Int,
        amount: Float,
        leverage: Int,
    ) {
        viewModelScope.launch {
            if (leverage > 0) {
                val result = validateExposureUseCase(ExposureRequest(id, amount, leverage))
                if (result is Result.Error) {
                    val exception: PositionExposureException =
                        (result.exception as PositionExposureException)
                    _exposureError.emit(exception)
                }
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

    private fun getAmount(
        instrumentId: Int,
        price: Float,
        leverage: Int,
        unit: Float,
    ) {
        viewModelScope.launch {
            val request = CalAmountUnitRequest(
                instrumentId = instrumentId,
                leverage = leverage,
                price = price,
                amount = unit
            )
            val result = getAmountUseCase(request)
            _amountState.value = _amountState.value.copy(amount = result.successOr(0f), unit = unit)
        }
    }


    private fun getUnit(
        instrumentId: Int,
        price: Float,
        leverage: Int,
        amount: Float,
    ) {
        viewModelScope.launch {
            val request = CalAmountUnitRequest(
                instrumentId = instrumentId,
                leverage = leverage,
                price = price,
                amount = amount
            )
            val result = getUnitUseCase(request)
            _amountState.value =
                _amountState.value.copy(amount = amount, unit = result.successOr(0f))
        }
    }

    fun updateStopLoss(number: Float, orderType: String, instrumentId: Int, openPrice: Float) {
        viewModelScope.launch {
            val stoploss = _stopLossState.value?.copy()
            when (stoploss?.type) {
                "amount" -> {
                    stoploss.amount = number * -1
                    getRateStopLoss(
                        stoploss,
                        instrumentId,
                        openPrice,
                        orderType
                    )
                }
                "rate" -> {
                    stoploss.unit = number
                    getAmountStopLoss(
                        stoploss,
                        instrumentId,
                        openPrice,
                        orderType
                    )
                }
                else -> null
            }

        }
    }

    private fun getAmountStopLoss(
        stoploss: Price,
        instrumentId: Int,
        openPrice: Float,
        orderType: String,
    ) {
        viewModelScope.launch {
            val amount = _amountState.value.copy()
            val result = when (orderType) {
                "sell" -> calculateAmountStopLossAndTakeProfitWithSellUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
                else -> calculateAmountStopLossAndTakeProfitWithBuyUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
            }

            if (result is Result.Success) {
                _stopLossState.emit(result.data)
            }
        }
    }

    private fun getAmountTakeProfit(
        stoploss: Price,
        instrumentId: Int,
        openPrice: Float,
        orderType: String,
    ) {
        viewModelScope.launch {
            val amount = _amountState.value.copy()
            val result = when (orderType) {
                "sell" -> calculateAmountStopLossAndTakeProfitWithSellUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
                else -> calculateAmountStopLossAndTakeProfitWithBuyUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
            }

            if (result is Result.Success) {
                _stopLossState.emit(result.data)
            }
        }
    }

    private fun getRateStopLoss(
        stoploss: Price,
        instrumentId: Int,
        openPrice: Float,
        orderType: String,
    ) {
        viewModelScope.launch {
            val amount = _amountState.value.copy()
            val result = when (orderType) {
                "sell" -> calculateRateStopLossAndTakeProfitsWithSellUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
                else -> calculateRateStopLossAndTakeProfitWithBuyUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
            }
            if (result is Result.Success) {
                _stopLossState.emit(result.data)
            }
        }
    }

    private fun getRateTakeProfit(
        stoploss: Price,
        instrumentId: Int,
        openPrice: Float,
        orderType: String,
    ) {
        viewModelScope.launch {
            val amount = _amountState.value.copy()
            val result = when (orderType) {
                "sell" -> calculateRateStopLossAndTakeProfitsWithSellUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
                else -> calculateRateStopLossAndTakeProfitWithBuyUseCase(
                    StopLossRequest(
                        instrumentId = instrumentId,
                        stopLoss = stoploss,
                        openPrice = openPrice,
                        unit = amount.unit
                    )
                )
            }
            if (result is Result.Success) {
                _takeProfit.emit(result.data)
            }
        }
    }

    fun getDetail(available: Float) {
//        viewModelScope.launch {
//            var message = ""
//            val amount = _amountState.value
//            val leverage = leverageState.value
//            if (available < amount.amount) {
//                _buttonText.value = "Deposit"
//                message = "Deposit %.2f $ for order to open this trade.".format(
//                    amount.amount.minus(available)
//                )
//
//            } else {
//                _buttonText.value = "Open Trade"
//                val amountUnit: Float = when (amount.type) {
//                    "amount" -> amount.unit
//                    else -> amount.amount
//                }
//                val equity: Float = (amount.amount.div(available)) * 100
//                message = "%.2f Units | %.2f%s of equity | Exposure $%.2f".format(
//                    amountUnit,
//                    equity,
//                    "%",
//                    amount.amount.times(leverage)
//                )
//            }
//            _detailState.emit(message)
//        }
    }

    fun updateTakeProfitType(type: String) {
        viewModelScope.launch {
            _takeProfit.value = _takeProfit.value?.copy(type = type)
        }
    }

    fun validateRate(rate: Float, price: Float, digit: Int) {
        viewModelScope.launch {
            val result = validateRateUseCase(ValidateRateRequest(rate = rate,
                currentRate = price,
                digit = digit))
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