package com.awonar.app.ui.order.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.shared.domain.market.GetConversionByInstrumentUseCase
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.ConverterOrderUtil
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPositionViewModel @Inject constructor(
    private val getConversionByInstrumentUseCase: GetConversionByInstrumentUseCase
) : ViewModel() {

    private val _isBuyState = MutableStateFlow(true)
    private val _instrumentId = MutableStateFlow(0)
    private val _tpRate = MutableStateFlow(0f)
    private val _units = MutableStateFlow(0f)
    private val _takeProfitState = MutableStateFlow(Pair(0f, 0f))
    val takeProfitState get() = _takeProfitState

    init {
        val flow = combine(
            _tpRate,
            _units,
            _instrumentId,
            _isBuyState
        ) { tpRate, units, instrumentId, isBuy ->
            val conversionRate = getConversionByInstrumentUseCase(instrumentId).successOr(0f)
            val openRate = QuoteSteamingManager.quotesState.value[instrumentId]?.ask
            val tpAmount = ConverterOrderUtil.convertRateToAmount(
                tpRate,
                conversionRate,
                units,
                openRate ?: 0f,
                isBuy
            )
            Pair(tpAmount, tpRate)
        }
        viewModelScope.launch {
            flow.collectIndexed { _, value ->
                _takeProfitState.value = value
            }
        }
    }

    fun setUnits(units: Float) {
        _units.value = units
    }

    fun setTakeProfit(tpRate: Float) {
        _tpRate.value = tpRate
    }

    fun setPositionType(isBuy: Boolean) {
        _isBuyState.value = isBuy
    }

    fun edit(id: String) {

    }

    fun setInstrumentId(i: Int) {
        _instrumentId.value = i
    }


}