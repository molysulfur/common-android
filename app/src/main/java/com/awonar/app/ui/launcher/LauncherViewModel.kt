package com.awonar.app.ui.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awonar.android.model.Conversion
import com.awonar.android.model.market.Instrument
import com.awonar.android.model.tradingdata.TradingData
import com.awonar.android.shared.domain.market.GetInstrumentListUseCase
import com.awonar.android.shared.domain.order.GetConversionsUseCase
import com.awonar.android.shared.domain.order.GetTradingDataUseCase
import com.awonar.android.shared.steaming.QuoteSteamingEvent
import com.awonar.android.shared.steaming.QuoteSteamingManager
import com.awonar.android.shared.utils.WhileViewSubscribed
import com.molysulfur.library.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val getTradingDataUseCase: GetTradingDataUseCase,
    private val getConversionUseCase: GetConversionsUseCase,
    private val getInstrumentListUseCase: GetInstrumentListUseCase,
    private val quoteSteamingManager: QuoteSteamingManager,
) : ViewModel() {

    private val instruments: StateFlow<List<Instrument>> = getInstrumentListUseCase(true).map {
        val list = it.successOr(emptyList()) ?: emptyList()
        list
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    private val loadTradingData: StateFlow<List<TradingData>?> = flow {
        getTradingDataUseCase(Unit).collect {
            val isAuth = it.successOr(emptyList())
            emit(isAuth)
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, null)

    private val loadConversionState: StateFlow<List<Conversion>> = flow {
        getConversionUseCase(Unit).collect {
            emit(it.successOr(emptyList()))
        }
    }.stateIn(viewModelScope, WhileViewSubscribed, emptyList())

    private val _navigateAction = Channel<Unit>()
    val navigateAction get() = _navigateAction.receiveAsFlow()

    init {
        val combine = combine(loadTradingData,
            loadConversionState,
            instruments) { trading, conversion, instruments ->
            if (trading != null) {
                if (instruments.isNotEmpty()) {
                    subscribe(instruments)
                    viewModelScope.launch {
                        _navigateAction.send(Unit)
                    }
                }

            }
        }
        viewModelScope.launch {
            combine.collect()
        }
    }

    fun subscribe(instruments: List<Instrument>) {
        val ids = instruments.map { instrument ->
            instrument.id
        }
        val joinIds = ids.joinToString {
            it.toString()
        }
        quoteSteamingManager.send(
            QuoteSteamingEvent.subscribe,
            joinIds
        )
    }

}
