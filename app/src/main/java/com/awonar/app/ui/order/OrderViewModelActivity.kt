package com.awonar.app.ui.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.constrant.MarketOrderType
import com.awonar.android.exception.PositionExposureException
import com.awonar.android.exception.RateException
import com.awonar.android.exception.ValidateStopLossException
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.order.*
import com.awonar.android.shared.domain.order.*
import com.molysulfur.library.result.Result
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val calculateAmountStopLossAndTakeProfitWithBuyUseCase: CalculateAmountStopLossAndTakeProfitWithBuyUseCase,
    private val calculateAmountStopLossAndTakeProfitWithSellUseCase: CalculateAmountStopLossAndTakeProfitWithSellUseCase,
    private val calculateRateStopLossAndTakeProfitWithBuyUseCase: CalculateRateStopLossAndTakeProfitWithBuyUseCase,
    private val calculateRateStopLossAndTakeProfitsWithSellUseCase: CalculateRateStopLossAndTakeProfitsWithSellUseCase,
    private val validateMinRateUseCase: ValidateMinRateUseCase,
    private val validateMaxRateUseCase: ValidateMaxRateUseCase,
    private val validateExposureUseCase: ValidateExposureUseCase,
    private val validateRateStopLossWithBuyUseCase: ValidateRateStopLossWithBuyUseCase,
    private val validateRateStopLossWithSellUseCase: ValidateRateStopLossWithSellUseCase,
    private val validateAmountStopLossWithBuyUseCase: ValidateAmountStopLossWithBuyUseCase,
    private val validateAmountStopLossWithSellUseCase: ValidateAmountStopLossWithSellUseCase,
    private val validateAmountStopLossWithNonLeverageBuyUseCase: ValidateAmountStopLossWithNonLeverageBuyUseCase,
    private val validateAmountStopLossWithNonLeverageSellUseCase: ValidateAmountStopLossWithNonLeverageSellUseCase,
) : ViewModel() {

    val leverageState = MutableStateFlow(0)
    private val _amountState = MutableStateFlow(Price(0f, 0f, "amount"))
    val amountState: StateFlow<Price> get() = _amountState
    private val _rateState = MutableStateFlow(0f)
    val rateState: StateFlow<Float> get() = _rateState
    private val _stopLossState = MutableStateFlow(Price(0f, 0f, "amount"))
    val stopLossState get() = _stopLossState
    private val _takeProfit = MutableStateFlow(Price(0f, 0f, "amount"))
    val takeProfit get() = _takeProfit

    val marketOrderTypeState = MutableStateFlow(MarketOrderType.ENTRY_ORDER)

    private val _exposureMessageState = MutableStateFlow("")
    val exposureMessageState: StateFlow<String> get() = _exposureMessageState
    private val _exposureState = MutableSharedFlow<Float>()
    val exposureState: SharedFlow<Float> get() = _exposureState

    fun validateStopLoss(
        instrument: Instrument,
        openPrice: Float,
        orderType: String
    ) {
        viewModelScope.launch {
            val stoploss = _stopLossState.value.copy()
            val data = ValidateStopLossRequest(
                instrumentId = instrument.id,
                stopLoss = stoploss,
                digit = instrument.digit,
                openPrice = openPrice
            )
            val result: Result<Price>? = when (stoploss.type) {
                "amount" -> validateAmountStopLoss(data, orderType)
                "rate" -> validateRateStopLoss(data, orderType)
                else -> null
            }
            Timber.e("$result")
            if (result is Result.Error) {
                val exception = result.exception as ValidateStopLossException
                updateStopLoss(exception.value, orderType, instrument.id, openPrice)
            }
        }
    }

    private suspend fun validateAmountStopLoss(
        data: ValidateStopLossRequest,
        type: String
    ): Result<Price> {
        val leverage = leverageState.value
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
                Result.Error(ValidateStopLossException("leverage or type was wrong!", 0f))
            }
        }
    }

    private suspend fun validateRateStopLoss(
        data: ValidateStopLossRequest,
        type: String
    ): Result<Price> = when (type) {
        "buy" -> validateRateStopLossWithBuyUseCase(
            data
        )
        "sell" -> validateRateStopLossWithSellUseCase(
            data
        )
        else -> Result.Error(ValidateStopLossException("type was wrong!", 0f))
    }

    fun updateStopLossType(type: String) {
        viewModelScope.launch {
            _stopLossState.value = _stopLossState.value.copy(type = type)
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

    fun validatePositionExposure(
        id: Int,
    ) {
        viewModelScope.launch {
            val amount = _amountState.value
            val leverage = leverageState.value
            if (leverage > 0) {
                val result = validateExposureUseCase(ExposureRequest(id, amount.amount, leverage))
                if (result is Result.Error) {
                    val exception: PositionExposureException =
                        (result.exception as PositionExposureException)
                    _exposureState.emit(exception.value)
                    _exposureMessageState.emit(exception.message ?: "")
                }
            }
        }
    }


    fun getDefaultAmount(instrumentId: Int, price: Float, available: Float) {
        viewModelScope.launch {
            val amount: Float = available.times(0.05f)
            val leverage: Int = leverageState.value
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

    fun updateLeverage(instrumentId: Int, price: Float, newLeverage: Int) {
        viewModelScope.launch {
            leverageState.emit(newLeverage)
            updateAmount(instrumentId, price, amountState.value.amount)
        }
    }

    fun updateAmount(instrumentId: Int, price: Float, number: Float) {
        viewModelScope.launch {
            val amount: Price = _amountState.value
            val leverage = leverageState.value
            when (amount.type) {
                "amount" -> {
                    getUnit(instrumentId, price, leverage, number)
                }
                "unit" -> {
                    getAmount(instrumentId, price, leverage, number)
                }
            }
        }
    }

    private fun getAmount(
        instrumentId: Int,
        price: Float,
        leverage: Int,
        unit: Float
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
        amount: Float
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
            val stoploss = _stopLossState.value.copy()
            when (stoploss.type) {
                "amount" -> {
                    stoploss.amount = number
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

}