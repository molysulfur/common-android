package com.awonar.app.ui.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.domain.market.GetInstrumentListUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.domain.ConvertInstrumentToItemUseCase
import com.awonar.app.ui.market.adapter.InstrumentItem
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getInstrumentListUseCase: GetInstrumentListUseCase,
    private val convertInstrumentToItemUseCase: ConvertInstrumentToItemUseCase
) : ViewModel() {

    private val _instrumentItem =
        MutableStateFlow<List<InstrumentItem>>(arrayListOf(InstrumentItem.LoadingItem()))
    val instrumentItem: StateFlow<List<InstrumentItem>> get() = _instrumentItem

    val instruments: StateFlow<List<Instrument>> = getInstrumentListUseCase(Unit).map {
        it.successOr(emptyList()) ?: emptyList()
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    fun convertInstrumentToItem(instruments: List<Instrument>) {
        viewModelScope.launch {
            _instrumentItem.value =
                convertInstrumentToItemUseCase(instruments).data ?: arrayListOf()
        }
    }
}