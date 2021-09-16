package com.awonar.app.ui.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.market.Instrument
import com.awonar.android.shared.domain.market.GetInstrumentListUseCase
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.domain.*
import com.awonar.app.ui.market.adapter.InstrumentItem
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getInstrumentListUseCase: GetInstrumentListUseCase,
    private val convertInstrumentToItemUseCase: ConvertInstrumentToItemUseCase,
    private val convertInstrumentStockToItemUseCase: ConvertInstrumentStockToItemUseCase,
    private val convertInstrumentCryptoToItemUseCase: ConvertInstrumentCryptoToItemUseCase,
    private val convertInstrumentCurrenciesToItemUseCase: ConvertInstrumentCurrenciesToItemUseCase,
    private val convertInstrumentETFsToItemUseCase: ConvertInstrumentETFsToItemUseCase,
    private val convertInstrumentCommodityToItemUseCase: ConvertInstrumentCommodityToItemUseCase
) : ViewModel() {
    private val _marketTabState =
        MutableSharedFlow<MarketFragment.Companion.MarketTabSelectedState>(replay = 0)
    val marketTabState: SharedFlow<MarketFragment.Companion.MarketTabSelectedState> =
        _marketTabState

    private val _instrumentItem =
        MutableStateFlow<List<InstrumentItem>>(arrayListOf(InstrumentItem.LoadingItem()))
    val instrumentItem: StateFlow<List<InstrumentItem>> get() = _instrumentItem

    val instruments: StateFlow<List<Instrument>> = getInstrumentListUseCase(Unit).map {
        it.successOr(emptyList()) ?: emptyList()
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    fun convertInstrumentToItem() {
        viewModelScope.launch {
            _instrumentItem.value =
                convertInstrumentToItemUseCase(instruments.value).data ?: arrayListOf()
        }
    }

    fun convertInstrumentCategoryToItem() {
        viewModelScope.launch {
            _instrumentItem.value =
                convertInstrumentStockToItemUseCase(instruments.value).data ?: arrayListOf()
        }
    }

    fun convertInstrumentCryptoToItem() {
        viewModelScope.launch {
            _instrumentItem.value =
                convertInstrumentCryptoToItemUseCase(instruments.value).data ?: arrayListOf()
        }
    }

    fun convertInstrumentCurrenciesToItem() {
        viewModelScope.launch {
            _instrumentItem.value =
                convertInstrumentCurrenciesToItemUseCase(instruments.value).data ?: arrayListOf()
        }
    }

    fun convertInstrumentETFsToItem() {
        viewModelScope.launch {
            _instrumentItem.value =
                convertInstrumentETFsToItemUseCase(instruments.value).data ?: arrayListOf()
        }
    }

    fun convertInstrumentCommodityToItem() {
        viewModelScope.launch {
            _instrumentItem.value =
                convertInstrumentCommodityToItemUseCase(instruments.value).data ?: arrayListOf()
        }
    }

    fun tabMarketStateChange(tabSelected: MarketFragment.Companion.MarketTabSelectedState) {
        viewModelScope.launch {
            _marketTabState.emit(tabSelected)
        }
    }
}