package com.awonar.app.ui.market

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.market.MarketViewMoreArg
import com.awonar.android.model.market.Quote
import com.awonar.android.model.order.OpenOrderRequest
import com.awonar.android.model.order.Price
import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.shared.domain.market.GetInstrumentListUseCase
import com.awonar.android.shared.domain.order.GetTradingDataByInstrumentIdUseCase
import com.awonar.android.shared.steaming.QuoteSteamingEvent
import com.awonar.android.shared.steaming.QuoteSteamingListener
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.awonar.app.domain.*
import com.awonar.app.ui.market.adapter.InstrumentItem
import com.molysulfur.library.result.data
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getInstrumentListUseCase: GetInstrumentListUseCase,
    private val convertRememberToItemUseCase: ConvertRememberToItemUseCase,
    private val convertInstrumentStockToItemUseCase: ConvertInstrumentStockToItemUseCase,
    private val convertInstrumentToItemUseCase: ConvertInstrumentToItemUseCase,
    private val getTradingDataByInstrumentIdUseCase: GetTradingDataByInstrumentIdUseCase,
    private val quoteSteamingManager: QuoteSteamingManager
) : ViewModel() {

    val tradingDataState = MutableSharedFlow<TradingData?>()

    private val _viewMoreState = Channel<MarketViewMoreArg?>(capacity = Channel.CONFLATED)
    val viewMoreState = _viewMoreState.receiveAsFlow()

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

    private val listener = object : QuoteSteamingListener {
        override fun marketStatusCallback(event: String, data: Any) {
        }

        override fun marketQuoteCallback(event: String, data: Array<Quote>) {
            _quoteSteamingState.value = data
        }
    }

    fun getTradingData(intrumentId: Int) {
        viewModelScope.launch {
            getTradingDataByInstrumentIdUseCase(intrumentId).collect {
                tradingDataState.emit(it.successOr(null))
            }
        }
    }


    fun setNewQuoteListener() {
        quoteSteamingManager.setListener(listener)
    }

    fun convertInstrumentToItem() {
        viewModelScope.launch {
            _instrumentItem.value =
                convertRememberToItemUseCase(instruments.value).data ?: arrayListOf()
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
            val instrumentList =
                instruments.value.filter { it.categories?.contains("crypto") == true }
            _instrumentItem.value =
                convertInstrumentToItemUseCase(instrumentList).data ?: arrayListOf()
        }
    }

    fun convertInstrumentCurrenciesToItem() {
        viewModelScope.launch {
            val instrumentList =
                instruments.value.filter { it.categories?.contains("currencies") == true }
            _instrumentItem.value =
                convertInstrumentToItemUseCase(instrumentList).data ?: arrayListOf()
        }
    }

    fun convertInstrumentETFsToItem() {
        viewModelScope.launch {
            val instrumentList = instruments.value.filter { it.categories?.contains("etf") == true }
            _instrumentItem.value =
                convertInstrumentToItemUseCase(instrumentList).data ?: arrayListOf()
        }
    }

    fun convertInstrumentCommodityToItem() {
        viewModelScope.launch {
            val instrumentList =
                instruments.value.filter { it.categories?.contains("commodity") == true }
            _instrumentItem.value =
                convertInstrumentToItemUseCase(instrumentList).data ?: arrayListOf()
        }
    }

    fun tabMarketStateChange(tabSelected: MarketFragment.Companion.MarketTabSelectedState) {
        viewModelScope.launch {
            _marketTabState.emit(tabSelected)
        }
    }

    fun subscribe(id: Int) {
        quoteSteamingManager.send(
            QuoteSteamingEvent.subscribe,
            "$id"
        )
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

    fun instrumentWithSector(sector: String) {
        viewModelScope.launch {
            val instrumentList = instruments.value.filter { it.sector?.equals(sector) ?: false }
            _instrumentItem.value =
                convertInstrumentToItemUseCase(instrumentList).data ?: arrayListOf()
        }
    }

    fun onViewMore(it: MarketViewMoreArg) {
        viewModelScope.launch {
            _viewMoreState.send(it)
        }
    }

    fun instrumentWithCategory(instrumentType: String?) {
        viewModelScope.launch {
            val instrumentList =
                instruments.value.filter { it.categories?.contains(instrumentType) ?: false }
            _instrumentItem.value =
                convertInstrumentToItemUseCase(instrumentList).data ?: arrayListOf()
        }
    }

}