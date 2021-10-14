package com.awonar.app.ui.market

import androidx.lifecycle.ViewModel
import com.awonar.android.constrant.MarketOrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OrderViewModelActivity @Inject constructor() : ViewModel() {

    private val _orderTypeState: MutableStateFlow<MarketOrderType> =
        MutableStateFlow(MarketOrderType.ENTRY_ORDER)
    val orderTypeState: StateFlow<MarketOrderType> get() = _orderTypeState


    fun updateMarketType(type: MarketOrderType) {
        _orderTypeState.value = type
    }
}