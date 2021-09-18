package com.awonar.app.ui.market

import android.service.autofill.Validators.or
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.Quote
import com.awonar.android.shared.domain.market.GetInstrumentListUseCase
import com.awonar.android.shared.domain.market.GetQuoteSteamingUseCase
import com.awonar.android.shared.steaming.QuoteSteamingEvent
import com.awonar.android.shared.steaming.QuoteSteamingListener
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.domain.*
import com.awonar.app.ui.market.adapter.InstrumentItem
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getInstrumentListUseCase: GetInstrumentListUseCase,
    private val convertInstrumentToItemUseCase: ConvertInstrumentToItemUseCase,
    private val convertInstrumentStockToItemUseCase: ConvertInstrumentStockToItemUseCase,
    private val convertInstrumentCryptoToItemUseCase: ConvertInstrumentCryptoToItemUseCase,
    private val convertInstrumentCurrenciesToItemUseCase: ConvertInstrumentCurrenciesToItemUseCase,
    private val convertInstrumentETFsToItemUseCase: ConvertInstrumentETFsToItemUseCase,
    private val convertInstrumentCommodityToItemUseCase: ConvertInstrumentCommodityToItemUseCase,
    private val getQuoteSteamingUseCase: GetQuoteSteamingUseCase,
    private val quoteSteamingManager: QuoteSteamingManager
) : ViewModel() {

    private val _quoteSteamingState = MutableStateFlow<Array<Quote>>(emptyArray())
    val quoteSteamingState: StateFlow<Array<Quote>> get() = _quoteSteamingState

    private val _marketTabState =
        MutableSharedFlow<MarketFragment.Companion.MarketTabSelectedState>(replay = 0)
    val marketTabState: SharedFlow<MarketFragment.Companion.MarketTabSelectedState> =
        _marketTabState

    private val _instrumentItem =
        MutableStateFlow<List<InstrumentItem>>(arrayListOf(InstrumentItem.LoadingItem()))
    val instrumentItem: StateFlow<List<InstrumentItem>> get() = _instrumentItem

    val instruments: StateFlow<List<Instrument>> = getInstrumentListUseCase(Unit).map {
        val list = it.successOr(emptyList()) ?: emptyList()
        list
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    val listener = object : QuoteSteamingListener {
        override fun marketStatusCallback(event: String, data: Any) {
        }

        override fun marketQuoteCallback(event: String, data: Array<Quote>) {
            _quoteSteamingState.value = data
        }

    }

    init {
        quoteSteamingManager.setListener(listener)
    }

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

    fun subscribe() {
        viewModelScope.launch {
            instruments.collect { instruments ->
                instruments.forEach { instrument ->
                    quoteSteamingManager.send(
                        QuoteSteamingEvent.subscribe,
                        "${instrument.id}"
                    )
                }

            }
        }
    }

    fun unsubscribe() {
        viewModelScope.launch {
            instruments.collect { instruments ->
                instruments.forEach { instrument ->
                    quoteSteamingManager.send(
                        QuoteSteamingEvent.subscribe,
                        "${instrument.id}"
                    )
                }

            }
        }
    }
}